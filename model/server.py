# server.py
import os, base64, json, cv2, numpy as np
from collections import deque
from fastapi import FastAPI, WebSocket
from fastapi.middleware.cors import CORSMiddleware
from starlette.websockets import WebSocketDisconnect
import pickle

MODEL_PATH = os.environ.get("MODEL_PATH", "model_rf_336.p")

LETTER_SET = set(list("ABCDEFGHIKLMNOPQRSTUVWXY") + ["J", "Z"])
NUMBER_SET = set(list("0123456789"))
MOTION_ONLY_CLASSES = {"J", "Z"}
MOTION_THRESHOLD = 0.05

SMOOTH_K = 7
SEQ_WINDOW = 30
MIN_SEQ_FOR_PRED = 8


def get_allowed_names(mode: str):
    m = (mode or "auto").lower()
    if m == "letters": return LETTER_SET
    if m == "numbers": return NUMBER_SET
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


def initialize_mediapipe():
    import mediapipe as mp
    mp_hands = mp.solutions.hands
    hands = mp_hands.Hands(
        static_image_mode=False,
        max_num_hands=2,
        min_detection_confidence=0.5,
        min_tracking_confidence=0.3
    )
    return mp_hands, hands


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


def feat84_from_results(results):
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


def to_336_from_seq(seq_Tx84):
    T = seq_Tx84.shape[0]
    mean = seq_Tx84.mean(axis=0)
    std = seq_Tx84.std(axis=0)
    last_first = seq_Tx84[-1] - seq_Tx84[0] if T > 1 else np.zeros_like(mean)
    mad = np.mean(np.abs(np.diff(seq_Tx84, axis=0)), axis=0) if T >= 2 else np.zeros_like(mean)
    return np.concatenate([mean, std, last_first, mad], axis=0).astype(np.float32)


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


# =========================
# ---- INIT MODEL + MP
# =========================
model, label_names, n_features = load_model_safely(MODEL_PATH)
mp_hands, hands = initialize_mediapipe()


# =========================
# ---- FASTAPI APP
# =========================
app = FastAPI()
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])


@app.get("/health")
def health():
    return {"ok": True, "n_features": int(n_features), "n_classes": len(label_names)}


@app.websocket("/ws")
async def ws_infer(ws: WebSocket):
    await ws.accept()
    print("WS: client connected")
    mode = "letters"
    feat84_buffer = deque(maxlen=max(SEQ_WINDOW, SMOOTH_K))
    try:
        while True:
            text = await ws.receive_text()
            #print("WS: recv text len:", len(text))
            data = json.loads(text)
            #print("WS: type =", data.get("type"))

            if data.get("type") == "set_mode":
                mode = data.get("mode", "letters")
                feat84_buffer.clear()
                await ws.send_text(json.dumps({"ok": True, "mode": mode}))
                continue

            if data.get("type") != "frame":
                continue

            jpeg_b64 = data.get("jpeg_b64")
            if not jpeg_b64:
                await ws.send_text(json.dumps({"error": "missing jpeg_b64"}))
                continue

            try:
                if "," in jpeg_b64:
                    jpeg_b64 = jpeg_b64.split(",", 1)[1]
                jpg_bytes = base64.b64decode(jpeg_b64)
            except Exception:
                await ws.send_text(json.dumps({"error": "b64_decode_failed"}))
                continue

            img = cv2.imdecode(np.frombuffer(jpg_bytes, np.uint8), cv2.IMREAD_COLOR)
            #print("frame:", "ok" if img is not None else "none", img.shape if img is not None else None)
            if img is None:
                await ws.send_text(json.dumps({"error": "decode_failed"}))
                continue

            results = hands.process(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
            #print("hands:", 0 if not getattr(results, "multi_hand_landmarks", None) else len(results.multi_hand_landmarks))

            feat84, _ = feat84_from_results(results)
            pred_proba = None
            motion_level = None

            if feat84 is not None:
                feat84_buffer.append(feat84)

                if n_features == 84 and len(feat84_buffer) >= SMOOTH_K:
                    X = np.mean(np.stack(list(feat84_buffer)[-SMOOTH_K:]), axis=0).reshape(1, -1)
                    pred_proba = model.predict_proba(X)[0]

                elif n_features == 336 and len(feat84_buffer) >= MIN_SEQ_FOR_PRED:
                    seq = np.stack(list(feat84_buffer)[-SEQ_WINDOW:], axis=0)
                    motion_level = window_motion_level(seq)
                    X336 = to_336_from_seq(seq)
                    pred_proba = model.predict_proba(X336.reshape(1, -1))[0]

                    if motion_level < MOTION_THRESHOLD:
                        for i, name in enumerate(label_names):
                            if name in MOTION_ONLY_CLASSES:
                                pred_proba[i] = 0.0
                        s = pred_proba.sum()
                        if s > 0:
                            pred_proba /= s

            if pred_proba is None:
                await ws.send_text(json.dumps({"status": "nohands"}))
                continue

            allowed = get_allowed_names(mode)
            pred_proba = mask_probs(pred_proba, label_names, allowed)

            ti = int(np.argmax(pred_proba))
            tp = float(pred_proba[ti])
            tl = label_names[ti]
            top3_idx = np.argsort(pred_proba)[::-1][:3]
            top3 = [(label_names[i], float(pred_proba[i])) for i in top3_idx]

            await ws.send_text(json.dumps({
                "top": {"label": tl, "prob": tp},
                "top3": top3,
                "motion": motion_level
            }))

    except WebSocketDisconnect:
        print("WS: disconnected")
    except Exception as e:
        print("WS: exception", e)
        try:
            await ws.send_text(json.dumps({"error": f"server_exception: {e.__class__.__name__}: {e}"}))
        except Exception:
            pass


# =========================
# ---- MAIN
# =========================
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("server:app", host="0.0.0.0", port=8000, reload=False)