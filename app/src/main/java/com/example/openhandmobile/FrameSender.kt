package com.example.openhandmobile


import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.openhandmobile.InferenceWs
import java.io.ByteArrayOutputStream
import kotlin.math.min

class FrameSender(
    private val ws: InferenceWs,
    private val contextProvider: () -> Context
) : ImageAnalysis.Analyzer {

    private var lastSent = 0L

    override fun analyze(image: ImageProxy) {
        val now = System.currentTimeMillis()
        if (now - lastSent < 90) { // ~11 FPS cap
            image.close()
            return
        }
        try {
            val bmp = image.toBitmap(mirrorHorizontally = false)
            val shortSide = min(bmp.width, bmp.height).toFloat()
            val scale = 320f / shortSide
            val scaled = Bitmap.createScaledBitmap(
                bmp, (bmp.width * scale).toInt(), (bmp.height * scale).toInt(), true
            )
            val baos = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, 40, baos)
            val b64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
            ws.sendJpegBase64(b64)
            lastSent = now
        } catch (_: Exception) {
            // ignore for demo
        } finally {
            image.close()
        }
    }
}