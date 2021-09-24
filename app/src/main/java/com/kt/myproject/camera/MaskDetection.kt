package com.kt.myproject.camera

import android.content.Context
import androidx.core.graphics.scale
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.kt.myproject.utils.Logger
import wee.digital.alfar.utils.camera.toBitmap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MaskDetection(context: Context) {

    companion object {
        const val TF_OD_API_INPUT_SIZE = 224
        const val TF_OD_API_IS_QUANTIZED = false
        const val MINIMUM_CONFIDENCE_TF_OD_API = 1.0f
    }

    private var ct = context

    private val log = Logger("MaskDetection")

    private var moduleMask: Classifier? = null

    private var processing = false

    var event: MaskEvent? = null

    private val executorFaceDetect: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        try {
            moduleMask = TFLiteObjectDetectionAPIModel.create(
                context.assets,
                "mask_tflite/mask_detector.tflite",
                "mask_tflite/mask_labelmap.txt",
                TF_OD_API_INPUT_SIZE,
                TF_OD_API_IS_QUANTIZED
            )
        } catch (e: Exception) {
            log.d("initModule Error: ${e.message}")
        }
    }

    fun detectMask(fullFrame: ByteArray, face: FirebaseVisionFace) {
        if (processing) return
        processing = true
        executorFaceDetect.submit {
            val bitmap = fullFrame.toBitmap()?.let { cropBitmapWithRect(it, face.boundingBox) }
            val scale = bitmap?.scale(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, true)
            if (scale == null) {
                bitmap?.recycle()
                scale?.recycle()
            } else {
                val resultsAux: List<Classifier.Recognition>? =
                    moduleMask?.recognizeImage(scale)
                val result = resultsAux?.firstOrNull()
                log.d("${result?.confidence} ${result?.title}")
                if (result.checkTitleMaskFace()) {
                    bitmap.recycle()
                    scale.recycle()
                    event?.onFaceMask(true, result?.confidence ?: 0f)
                } else {
                    log.d("face mask")
                    event?.onFaceMask(false, result?.confidence ?: 0f)
                }
            }
            processing = false
        }
    }

    fun onDestroy() {
        moduleMask?.close()
        executorFaceDetect.shutdownNow()
    }

    interface MaskEvent {
        fun onFaceMask(bool: Boolean, confidence: Float)
    }

}