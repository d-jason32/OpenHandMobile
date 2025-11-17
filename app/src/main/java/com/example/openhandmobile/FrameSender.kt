package com.example.openhandmobile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Base64
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import kotlin.math.min

class FrameSender(
    private val ws: InferenceWs,
    private val contextProvider: () -> Context
) : ImageAnalysis.Analyzer {

    companion object {
        // Send at most ~5 fps instead of ~11
        private const val MIN_INTERVAL_MS = 200L

        // Make the short side small = less data to compress
        //private const val TARGET_SHORT_SIDE = 224

        // Lower JPEG quality: good enough for landmarks
        private const val JPEG_QUALITY = 30
    }

    private var lastSent = 0L

    // Reuse the same buffer to avoid allocations every frame
    private val baos = ByteArrayOutputStream()

    override fun analyze(image: ImageProxy) {
        val now = System.currentTimeMillis()
        if (now - lastSent < MIN_INTERVAL_MS) {
            image.close()
            return
        }

        try {
            val nv21 = yuv420888ToNv21(image)
            val rect = Rect(0, 0, image.width, image.height)

            baos.reset()
            val yuv = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
            yuv.compressToJpeg(rect, JPEG_QUALITY, baos)

            val b64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
            ws.sendJpegBase64(b64)

            lastSent = now
        } catch (e: Exception) {}
        finally {
            image.close()
        }
    }
}
