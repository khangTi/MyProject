package com.kt.myproject.camera

import android.annotation.SuppressLint
import android.graphics.*
import android.media.Image
import android.util.DisplayMetrics
import android.util.Size
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * config extension
 */
const val WIDTH = 640

const val HEIGHT = 480

val SIZE = Size(WIDTH, HEIGHT)

const val RATIO_4_3_VALUE = 4.0 / 3.0

const val RATIO_16_9_VALUE = 16.0 / 9.0

fun ProcessCameraProvider?.cameraBack(): Boolean {
    return this?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
}

fun ProcessCameraProvider?.cameraFront(): Boolean {
    return this?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
}

fun DisplayMetrics.aspectRatio(): Int {
    val width = this.widthPixels
    val height = this.heightPixels
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}

/**
 * handle frame wee.digital.alfar.utils.camera
 */

typealias FrameResult = (nv21: ByteArray, width: Int, height: Int) -> Unit

typealias FrameEvent = (frame: ByteArray, width: Int, height: Int) -> Unit

class LuminosityAnalyzer(listener: FrameEvent? = null) : ImageAnalysis.Analyzer {

    private val frameRateWindow = 8

    private val frameTimestamps = ArrayDeque<Long>(5)

    private val listener = ArrayList<FrameEvent>().apply {
        listener?.let { add(it) }
    }

    private var lastAnalyzedTimestap = 0L

    var framesPerSecond: Double = -1.0
        private set

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(images: ImageProxy) {
        if (listener.isNullOrEmpty()) {
            images.close()
            return
        }
        val width = images.width
        val height = images.height
        images.image?.let {
            val nv21 = it.YUV420toNV21()
            listener.forEach { it(nv21, width, height) }
        }
        images.close()
    }
}

/**
 * frame utils
 */
fun Image.YUV420toNV21(): ByteArray {
    val crop = this.cropRect
    val format = this.format
    val width = crop.width()
    val height = crop.height()
    val planes = this.planes
    val data = ByteArray(width * height * ImageFormat.getBitsPerPixel(format) / 8)
    val rowData = ByteArray(planes[0].rowStride)
    var channelOffset = 0
    var outputStride = 1
    for (i in planes.indices) {
        when (i) {
            0 -> {
                channelOffset = 0
                outputStride = 1
            }
            1 -> {
                channelOffset = width * height + 1
                outputStride = 2
            }
            2 -> {
                channelOffset = width * height
                outputStride = 2
            }
        }
        val buffer = planes[i].buffer
        val rowStride = planes[i].rowStride
        val pixelStride = planes[i].pixelStride
        val shift = if (i == 0) 0 else 1
        val w = width shr shift
        val h = height shr shift
        buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
        for (row in 0 until h) {
            var length: Int
            if (pixelStride == 1 && outputStride == 1) {
                length = w
                buffer[data, channelOffset, length]
                channelOffset += length
            } else {
                length = (w - 1) * pixelStride + 1
                buffer[rowData, 0, length]
                for (col in 0 until w) {
                    data[channelOffset] = rowData[col * pixelStride]
                    channelOffset += outputStride
                }
            }
            if (row < h - 1) {
                buffer.position(buffer.position() + rowStride - length)
            }
        }
    }
    return data
}

fun ByteArray?.NV21toJPEG(): ByteArray? {
    this ?: return null
    val out = ByteArrayOutputStream()
    val yuv = YuvImage(this, ImageFormat.NV21, WIDTH, HEIGHT, null)
    yuv.compressToJpeg(Rect(0, 0, WIDTH, HEIGHT), 80, out)
    return out.toByteArray()
}

fun Bitmap?.bitmapToByteArray(): ByteArray? {
    this ?: return null
    return try {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        stream.close()
        byteArray
    } catch (e: Exception) {
        null
    }
}

fun ByteArray?.toBitmap(): Bitmap? {
    this ?: return null
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

fun ByteArray?.Nv21ToBitmap(): Bitmap? {
    this ?: return null
    return this.NV21toJPEG().toBitmap()
}

fun Bitmap?.rotate(degrees: Int): Bitmap? {
    this ?: return null
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Image.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}