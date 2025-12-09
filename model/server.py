# server.py
import os, base64, json, cv2, numpy as np
from collections import deque
from fastapi import FastAPI, WebSocket, Query
from fastapi.middleware.cors import CORSMiddleware
from starlette.websockets import WebSocketDisconnect
import pickle
from pathlib import Path

# =========================
# ---- MODEL PATHS
# =========================
MODEL_PATHS = {
    "letters": os.environ.get("MODEL_PATH", "model_rf_336.p"),
    "gestures": os.environ.get("PHRASE_MODEL_PATH", "model_rf_336_phrases.p"),
}
DEFAULT_MODEL_NAME = os.environ.get("DEFAULT_MODEL", "letters").lower()

LETTER_SET = set(list("ABCDEFGHIKLMNOPQRSTUVWXY") + ["J", "Z"])
NUMBER_SET = set(list("0123456789"))
MOTION_ONLY_CLASSES = {"J", "Z"}
MOTION_THRESHOLD = 0.05

SMOOTH_K = 7
SEQ_WINDOW = 30
MIN_SEQ_FOR_PRED = 8


def get_allowed_names(mode: str):
    m = (mode or "auto").lower()
    if m == "letters":
        return LETTER_SET
    if m == "numbers":
        return NUMBER_SET
    return None


def load_model_safely(model_path):
    if not os.path.exists(model_path):
        raise FileNotFoundError(f"Model file not found: {model_path}")
    with open(model_path, "rb") as f:
        obj = pickle.load(f)
    model = obj.get("model", obj)
    meta_classes = obj.get("classes", None)
    n_features = getattr(model, "n_features_in_", None)
    if isinstance(meta_classes, (list, tuple)) and len(meta_classes) > 0:
        label_names = [str(c) for c in meta_classes]
    else:
        label_names = [str(c) for c in getattr(model, "classes_", [])]
    if not label_names:
        raise RuntimeError("Could not resolve class names from model/meta.")
    print("=== MODEL CLASS INSPECTION ===")
    print(f"n_features: {n_features}")
    print(f"label_names ({len(label_names)}): {label_names}")
    return model, label_names, n_features


def format_display_label(raw: str) -> str:
    """Human-friendly label for UI (e.g., ALL_DONE -> All Done)."""
    s = str(raw).replace("_", " ").strip()
    if len(s) <= 3:  # keep short codes like A, B, J, Z, 1, 10
        return s.upper()
    return s.title()


def resolve_display_labels(name, labels, model_path):
    # Prefer external label_names.json for phrases if available
    candidates = []
    if name == "gestures":
        candidates.append(os.path.join(os.path.dirname(model_path), "label_names.json"))
        candidates.append(os.path.normpath(os.path.join(os.path.dirname(__file__), "../OpenHandWebApp/backend/model/label_names.json")))

    for cand in candidates:
        if os.path.exists(cand):
            try:
                loaded = json.loads(Path(cand).read_text(encoding="utf-8"))
                if len(loaded) == len(labels):
                    return [format_display_label(x) for x in loaded]
                else:
                    print(f"[model] label_names.json at {cand} length {len(loaded)} != model classes {len(labels)}")
            except Exception as exc:
                print(f"[model] failed to read label file {cand}: {exc}")

    return [format_display_label(l) for l in labels]


def load_available_models():
    registry = {}
    for name, path in MODEL_PATHS.items():
        try:
            model, labels, n_feats = load_model_safely(path)
            display_labels = resolve_display_labels(name, labels, path)
            registry[name] = {
                "model": model,
                "labels": labels,
                "display_labels": display_labels,
                "n_features": n_feats,
                "path": path,
            }
            print(f"[model] loaded '{name}' from {path} with {len(labels)} classes ({n_feats} feats)")
        except FileNotFoundError as exc:
            print(f"[model] missing '{name}' at {path}: {exc}")
        except Exception as exc:
            print(f"[model] failed to load '{name}' from {path}: {exc}")
    if not registry:
        raise RuntimeError("No models could be loaded.")
    return registry


MODEL_REGISTRY = load_available_models()
if DEFAULT_MODEL_NAME not in MODEL_REGISTRY:
    DEFAULT_MODEL_NAME = next(iter(MODEL_REGISTRY.keys()))
    print(f"[model] DEFAULT_MODEL not found, using '{DEFAULT_MODEL_NAME}'")


# =========================
# ---- FEATURE HELPERS
# =========================
def order_hands(results):
    if not getattr(results, "multi_hand_landmarks", None):
        return []
    hands_list = []
    if getattr(results, "multi_handedness", None):
        for handed, hl in zip(results.multi_handedness, results.multi_hand_landmarks):
            label = handed.classification[0].label
            conf = handed.classification[0].score
            hands_list.append((label, hl, conf))
    else:
        for hl in results.multi_hand_landmarks:
            mean_x = sum(lm.x for lm in hl.landmark) / 21.0
            label = "Left" if mean_x < 0.5 else "Right"
            hands_list.append((label, hl, 1.0))
    hands_list.sort(key=lambda x: sum(lm.x for lm in x[1].landmark) / 21.0)
    return hands_list[:2]


def feat84_letters(results):
    hlists = order_hands(results)
    if not hlists:
        return None, 0.0
    xs = [lm.x for _, hl, _ in hlists for lm in hl.landmark]
    ys = [lm.y for _, hl, _ in hlists for lm in hl.landmark]
    min_x, min_y = min(xs), min(ys)
    feat = []
    for slot in range(2):
        if slot < len(hlists):
            _, hl, _ = hlists[slot]
            for lm in hl.landmark:
                feat.extend([lm.x - min_x, lm.y - min_y])
        else:
            feat.extend([0.0] * 42)
    return np.asarray(feat, dtype=np.float32), 1.0


def feat84_gestures(results):
    left = None
    right = None
    if getattr(results, "multi_hand_landmarks", None) and getattr(results, "multi_handedness", None):
        for lm, hd in zip(results.multi_hand_landmarks, results.multi_handedness):
            label = hd.classification[0].label.lower()
            xy = np.array([(pt.x, pt.y) for pt in lm.landmark], dtype=np.float32)
            wrist = xy[0]
            xy -= wrist
            palm = np.linalg.norm(xy[9]) + 1e-6
            xy /= palm
            if label == "left":
                left = xy
            else:
                right = xy
    def pack(xy):
        return np.zeros(42, dtype=np.float32) if xy is None else xy.reshape(-1)
    feat = np.concatenate([pack(left), pack(right)], axis=0)
    if not np.any(feat):
        return None, 0.0
    return feat.astype(np.float32), 1.0


def to_336_letters(seq_Tx84):
    T = seq_Tx84.shape[0]
    mean = seq_Tx84.mean(axis=0)
    std = seq_Tx84.std(axis=0)
    last_first = seq_Tx84[-1] - seq_Tx84[0] if T > 1 else np.zeros_like(mean)
    mad = np.mean(np.abs(np.diff(seq_Tx84, axis=0)), axis=0) if T >= 2 else np.zeros_like(mean)
    return np.concatenate([mean, std, last_first, mad], axis=0).astype(np.float32)


def to_336_gestures(seq_Tx84):
    M = seq_Tx84
    mu = M.mean(axis=0)
    sd = M.std(axis=0) + 1e-6
    dM = np.diff(M, axis=0)
    dmu = dM.mean(axis=0)
    dsd = dM.std(axis=0) + 1e-6
    return np.concatenate([mu, sd, dmu, dsd], axis=0).astype(np.float32)


def window_motion_level(seq_Tx84):
    if seq_Tx84.shape[0] < 2:
        return 0.0
    return float(np.abs(np.diff(seq_Tx84, axis=0)).mean())


def mask_probs(probs, label_names, allowed):
    if allowed is None:
        return probs
    masked = probs.copy()
    for i, name in enumerate(label_names):
        if name not in allowed:
            masked[i] = 0.0
    s = masked.sum()
    if s > 0:
        masked /= s
    return masked


def decode_image(jpeg_b64: str):
    if not jpeg_b64:
        return None
    try:
        if "," in jpeg_b64:
            jpeg_b64 = jpeg_b64.split(",", 1)[1]
        jpg_bytes = base64.b64decode(jpeg_b64)
        img = cv2.imdecode(np.frombuffer(jpg_bytes, np.uint8), cv2.IMREAD_COLOR)
        return img
    except Exception:
        return None


# =========================
# ---- SESSIONS
# =========================
class LettersSession:
    def __init__(self, model_info):
        self.model_name = "letters"
        self.model = model_info["model"]
        self.labels = model_info["labels"]
        self.display_labels = model_info["display_labels"]
        self.n_features = model_info["n_features"]
        self.mode = "letters"
        self.feat84_buffer = deque(maxlen=max(SEQ_WINDOW, SMOOTH_K))
        self._init_hands()

    def _init_hands(self):
        import mediapipe as mp
        mp_hands = mp.solutions.hands
        self.hands = mp_hands.Hands(
            static_image_mode=False,
            max_num_hands=2,
            min_detection_confidence=0.5,
            min_tracking_confidence=0.3,
        )

    def set_mode(self, mode: str):
        m = (mode or "letters").lower()
        if m in ("letters", "numbers", "auto"):
            self.mode = m
            self.feat84_buffer.clear()

    def predict(self, img):
        results = self.hands.process(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
        feat84, _ = feat84_letters(results)
        pred_proba = None
        motion_level = None

        if feat84 is not None:
            self.feat84_buffer.append(feat84)

            if self.n_features == 84 and len(self.feat84_buffer) >= SMOOTH_K:
                X = np.mean(np.stack(list(self.feat84_buffer)[-SMOOTH_K:]), axis=0).reshape(1, -1)
                pred_proba = self.model.predict_proba(X)[0]

            elif self.n_features == 336 and len(self.feat84_buffer) >= MIN_SEQ_FOR_PRED:
                seq = np.stack(list(self.feat84_buffer)[-SEQ_WINDOW:], axis=0)
                motion_level = window_motion_level(seq)
                X336 = to_336_letters(seq)
                pred_proba = self.model.predict_proba(X336.reshape(1, -1))[0]

                if motion_level < MOTION_THRESHOLD:
                    for i, name in enumerate(self.labels):
                        if name in MOTION_ONLY_CLASSES:
                            pred_proba[i] = 0.0
                    s = pred_proba.sum()
                    if s > 0:
                        pred_proba /= s

        if pred_proba is not None:
            allowed = get_allowed_names(self.mode)
            pred_proba = mask_probs(pred_proba, self.labels, allowed)

        return pred_proba, motion_level

    def close(self):
        try:
            self.hands.close()
        except Exception:
            pass


class GesturesSession:
    SEQ_WINDOW = 30
    MIN_SEQ_FOR_PRED = 18
    PREDICT_STRIDE = 3
    FRAME_PROCESS_SKIP = 2

    def __init__(self, model_info):
        self.model_name = "gestures"
        self.model = model_info["model"]
        self.labels = model_info["labels"]
        self.display_labels = model_info["display_labels"]
        self.n_features = model_info["n_features"]
        self.feat84_buffer = deque(maxlen=self.SEQ_WINDOW)
        self.proba_buffer = deque(maxlen=6)
        self.frame_count = 0
        self.process_count = 0
        self.last_feat84 = None
        self._init_hands()

    def _init_hands(self):
        import mediapipe as mp
        mp_hands = mp.solutions.hands
        self.hands = mp_hands.Hands(
            static_image_mode=False,
            max_num_hands=2,
            model_complexity=0,
            min_detection_confidence=0.4,
            min_tracking_confidence=0.4,
        )

    def predict(self, img):
        self.frame_count += 1
        feat84 = None
        hand_conf = 0.0

        if self.frame_count % self.FRAME_PROCESS_SKIP == 0:
            results = self.hands.process(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
            feat84, hand_conf = feat84_gestures(results)
            if feat84 is not None:
                self.last_feat84 = feat84
                self.feat84_buffer.append(feat84)
            else:
                self.feat84_buffer.clear()
                self.last_feat84 = None
        elif self.last_feat84 is not None:
            feat84 = self.last_feat84
            hand_conf = 1.0
            self.feat84_buffer.append(feat84)

        pred_proba = None
        if feat84 is not None and len(self.feat84_buffer) >= self.MIN_SEQ_FOR_PRED:
            self.process_count += 1
            if self.process_count % self.PREDICT_STRIDE == 0:
                seq = np.stack(list(self.feat84_buffer)[-self.SEQ_WINDOW:], axis=0)
                X336 = to_336_gestures(seq).reshape(1, -1)
                if hasattr(self.model, "predict_proba"):
                    pred_proba = self.model.predict_proba(X336)[0]
                else:
                    pred_idx = int(self.model.predict(X336)[0])
                    pred_proba = np.zeros(len(self.labels), dtype=np.float32)
                    if 0 <= pred_idx < len(self.labels):
                        pred_proba[pred_idx] = 1.0
                self.proba_buffer.append(pred_proba)

        proba_display = None
        if self.proba_buffer:
            proba_display = np.mean(np.stack(self.proba_buffer, axis=0), axis=0)

        return proba_display, None

    def close(self):
        try:
            self.hands.close()
        except Exception:
            pass


def make_session(model_name: str):
    name = (model_name or DEFAULT_MODEL_NAME).lower()
    if name not in MODEL_REGISTRY:
        name = DEFAULT_MODEL_NAME
    info = MODEL_REGISTRY[name]
    if name == "gestures":
        return GesturesSession(info)
    return LettersSession(info)


# =========================
# ---- FASTAPI APP
# =========================
app = FastAPI()
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])


@app.get("/health")
def health():
    return {
        "ok": True,
        "default_model": DEFAULT_MODEL_NAME,
        "models": {
            name: {
                "n_features": int(info["n_features"]),
                "n_classes": len(info["labels"]),
                "path": info["path"],
            }
            for name, info in MODEL_REGISTRY.items()
        },
    }


def format_topk(pred_proba, labels, display_labels, top_k=3):
    ti = int(np.argmax(pred_proba))
    tp = float(pred_proba[ti])
    tl = display_labels[ti]
    topk_idx = np.argsort(pred_proba)[::-1][:top_k]
    topk = [(display_labels[i], float(pred_proba[i])) for i in topk_idx]
    return tl, tp, topk


@app.websocket("/ws")
async def ws_infer(ws: WebSocket, model: str = Query(default=DEFAULT_MODEL_NAME)):
    await ws.accept()
    session = make_session(model)
    print(f"WS: client connected (model={session.model_name})")
    try:
        await ws.send_text(json.dumps({
            "hello": True,
            "model": session.model_name,
            "n_features": int(session.n_features),
            "n_classes": len(session.labels),
        }))

        while True:
            text = await ws.receive_text()
            data = json.loads(text)

            if data.get("type") == "set_model":
                new_model = data.get("model", session.model_name)
                if new_model and new_model != session.model_name:
                    session.close()
                    session = make_session(new_model)
                    await ws.send_text(json.dumps({"ok": True, "model": session.model_name}))
                continue

            if data.get("type") == "set_mode" and isinstance(session, LettersSession):
                session.set_mode(data.get("mode", "letters"))
                await ws.send_text(json.dumps({"ok": True, "mode": session.mode}))
                continue

            if data.get("type") != "frame":
                continue

            img = decode_image(data.get("jpeg_b64"))
            if img is None:
                await ws.send_text(json.dumps({"error": "decode_failed"}))
                continue

            pred_proba, motion_level = session.predict(img)
            if pred_proba is None:
                await ws.send_text(json.dumps({"status": "nohands", "model": session.model_name}))
                continue

            tl, tp, top3 = format_topk(pred_proba, session.labels, session.display_labels)
            await ws.send_text(json.dumps({
                "top": {"label": tl, "prob": tp},
                "top3": top3,
                "motion": motion_level,
                "model": session.model_name,
            }))

    except WebSocketDisconnect:
        print("WS: disconnected")
    except Exception as e:
        print("WS: exception", e)
        try:
            await ws.send_text(json.dumps({"error": f"server_exception: {e.__class__.__name__}: {e}"}))
        except Exception:
            pass
    finally:
        session.close()


# =========================
# ---- MAIN
# =========================
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("server:app", host="0.0.0.0", port=8000, reload=False)