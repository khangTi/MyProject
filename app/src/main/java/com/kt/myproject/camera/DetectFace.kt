package com.kt.myproject.camera

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.kt.myproject.utils.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetectFace(context: Context) {

    private val log = Logger("MyDetection")

    private val ct = context

    private var countFaceNotFrame = 0

    private var countFaceEligible = 0

    private var isDetectFrame = false

    private var isFaceEligible = false

    var detectEvent: DetectCallBack? = null

    private var mFaceDetector: FirebaseVisionFaceDetector? = null

    private val executorFaceDetect: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        mFaceDetector = FirebaseApp.initializeApp(ct).let {
            FirebaseVision.getInstance().getVisionFaceDetector(MLKitFaceOption)
        }
    }

    fun mlKitDetectFrame(fullFrameColor: ByteArray) {
        if (isDetectFrame) return
        isDetectFrame = true
        val visionImage = FirebaseVisionImage.fromByteArray(fullFrameColor, mLKitMetadata)
        mFaceDetector?.detectInImage(visionImage)
            ?.addOnSuccessListener {
                try {
                    executorFaceDetect.submit {
                        val face = it.getLargestFace()
                        if (face == null) {
                            resetDetect()
                        } else {
                            optionFace(face, visionImage.bitmap)
                        }
                    }
                } catch (e: Exception) {
                    resetDetect()
                }
            }
            ?.addOnFailureListener {
                log.d(it.message.toString())
                resetDetect()
            }
    }

    private fun optionFace(
        face: FirebaseVisionFace,
        fullFrameColor: Bitmap
    ) {
        if (face.checkFaceOk()) {
            log.d("option face ok")

            val byteFullFrame = fullFrameColor.bitmapToByteArray()

            val data = fullFrameColor.getDataFaceAndFace(face)
                .apply { this.fullFrame = byteFullFrame }

            if (data.face != null && data.dataFace != null && data.fullFrame != null) {
                detectEvent?.faceOnFrame()
                eventFaceResult(face, data)
            } else {
                log.d("get data face null")
                resetDetect()
            }
            isDetectFrame = false
        } else {
            log.d("option face fail")
            resetDetect()
        }

    }

    private fun resetDetect() {
        isDetectFrame = false
        countFaceEligible = 0
        countFaceNotFrame++.also {
            if (it <= 3) return@also
            countFaceNotFrame = 0
            detectEvent?.faceNotFrame()
        }

    }

    private fun eventFaceResult(
        face: FirebaseVisionFace,
        dataFace: DataGetFacePoint
    ) {
        if (isFaceEligible) return
        isFaceEligible = true
        countFaceEligible++
        if (countFaceEligible > 1) {
            countFaceEligible = 0
            detectEvent?.faceEligible(face, dataFace)
        }
        isFaceEligible = false
    }


    private fun resetFaceEligible() {
        isFaceEligible = false
        countFaceEligible = 0
        detectEvent?.faceNotEligible()
    }

    fun destroyDetection() {
        try {
            executorFaceDetect.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface DetectCallBack {
        fun faceNotFrame() {}
        fun faceOnFrame() {}
        fun faceNotEligible() {}
        fun faceEligible(face: FirebaseVisionFace, dataFace: DataGetFacePoint) {}
    }

}