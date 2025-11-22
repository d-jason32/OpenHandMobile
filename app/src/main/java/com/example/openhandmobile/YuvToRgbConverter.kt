package com.example.openhandmobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.apply
import kotlin.ranges.until

@OptIn(ExperimentalGetImage::class)
fun ImageProxy.toBitmap(
    mirrorHorizontally: Boolean = false
): Bitmap {
    val nv21 = yuv420888ToNv21(this)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
    val jpegBytes = out.toByteArray()
    var bmp = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)

    val matrix = Matrix().apply {
        postRotate(imageInfo.rotationDegrees.toFloat())
        if (mirrorHorizontally) {
            postScale(-1f, 1f, bmp.width / 2f, bmp.height / 2f)
        }
    }
    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
    return bmp
}

private fun yuv420888ToNv21(image: ImageProxy): ByteArray {
    val yPlane = image.planes[0]
    val uPlane = image.planes[1]
    val vPlane = image.planes[2]

    val width = image.width
    val height = image.height

    val ySize = width * height
    val chromaWidth = width / 2
    val chromaHeight = height / 2
    val chromaSize = chromaWidth * chromaHeight * 2 // VU interleaved

    val nv21 = ByteArray(ySize + chromaSize)

    copyPlane(
        src = yPlane.buffer,
        rowStride = yPlane.rowStride,
        pixelStride = yPlane.pixelStride,
        width = width,
        height = height,
        dest = nv21,
        destOffset = 0,
        destPixelStride = 1
    )

    val uBuffer = uPlane.buffer
    val vBuffer = vPlane.buffer
    val uvRowStrideU = uPlane.rowStride
    val uvRowStrideV = vPlane.rowStride
    val uvPixelStrideU = uPlane.pixelStride
    val uvPixelStrideV = vPlane.pixelStride

    var destIndex = ySize
    val rowU = ByteArray(uvRowStrideU)
    val rowV = ByteArray(uvRowStrideV)

    for (row in 0 until chromaHeight) {
        readRow(uBuffer, rowU, uvRowStrideU)
        readRow(vBuffer, rowV, uvRowStrideV)

        var uIdx = 0
        var vIdx = 0
        for (col in 0 until chromaWidth) {
            val v = rowV[vIdx]
            val u = rowU[uIdx]
            nv21[destIndex++] = v
            nv21[destIndex++] = u
            uIdx += uvPixelStrideU
            vIdx += uvPixelStrideV
        }
    }

    return nv21
}

private fun readRow(buffer: ByteBuffer, dst: ByteArray, rowStride: Int) {
    val toRead = kotlin.comparisons.minOf(buffer.remaining(), rowStride)
    buffer.get(dst, 0, toRead)
    if (toRead < rowStride) {
        for (i in toRead until rowStride) dst[i] = 0
    }
}


private fun copyPlane(
    src: ByteBuffer,
    rowStride: Int,
    pixelStride: Int,
    width: Int,
    height: Int,
    dest: ByteArray,
    destOffset: Int,
    destPixelStride: Int
) {
    var srcPos = src.position()
    var destIndex = destOffset
    for (row in 0 until height) {
        var colSrc = srcPos
        for (col in 0 until width) {
            dest[destIndex + col * destPixelStride] = src.get(colSrc)
            colSrc += pixelStride
        }
        srcPos += rowStride
        src.position(srcPos)
        destIndex += width * destPixelStride
    }
}