package com.example.openhandmobile

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response
import com.google.gson.Gson
import java.util.concurrent.TimeUnit
import kotlin.jvm.java
import kotlin.text.take

class InferenceWs(
    private val url: String,
    private val onTop: (label: String, prob: Float) -> Unit
) {
    private val client = OkHttpClient.Builder()
        .pingInterval(15, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private var ws: WebSocket? = null

    fun connect() {
        val request = Request.Builder().url(url).build()
        ws = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                onTop("WS: connected", 0f)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    val obj = gson.fromJson(text, Map::class.java) as Map<String, Any?>

                    // Top prediction payload
                    val top = obj["top"]
                    if (top is Map<*, *>) {
                        val label = top["label"] as? String ?: "?"
                        val prob = (top["prob"] as? Number)?.toFloat() ?: 0f
                        onTop(label, prob)
                        return
                    }

                    when {
                        obj["status"] == "nohands" -> {
                            onTop("No hands", 0f); return
                        }
                        obj["ok"] == true && obj["mode"] is String -> {
                            onTop("Mode: ${obj["mode"] as String}", 0f); return
                        }
                        obj["error"] is String -> {
                            onTop("ERR: ${(obj["error"] as String).take(40)}", 0f); return
                        }
                        else -> {
                        }
                    }
                } catch (_: Exception) {
                    onTop("Parse err", 0f)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onTop("WS fail: ${t.message ?: "unknown"}", 0f)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                onTop("WS closing $code", 0f)
            }
        })
    }

    fun setMode(mode: String) {
        ws?.send("""{"type":"set_mode","mode":"$mode"}""")
    }

    fun setModel(model: String) {
        ws?.send("""{"type":"set_model","model":"$model"}""")
    }

    fun sendJpegBase64(b64: String) {
        ws?.send("""{"type":"frame","jpeg_b64":"$b64"}""")
    }

    fun close() {
        ws?.close(1000, "bye")
    }
}