package com.kt.myproject.camera

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import wee.digital.alfar.utils.camera.HEIGHT
import wee.digital.alfar.utils.camera.WIDTH
import wee.digital.alfar.utils.camera.bitmapToByteArray
import com.google.android.gms.vision.face.Landmark
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import com.kt.myproject.utils.Logger
import kotlin.math.roundToInt

private val log = Logger("DetectExtension")

val rangeX = -30f..30f

val rangeY = -30f..30f

val FRONT_FACE_VALUE = -20f..20f

val MLKitFaceOption = FirebaseVisionFaceDetectorOptions.Builder()
    .setClassificationMode(FirebaseVisionFaceDetectorOptions.NO_CLASSIFICATIONS)
    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
    .setContourMode(FirebaseVisionFaceDetectorOptions.NO_CONTOURS)
    .setMinFaceSize(0.5f)
    .build()

val mLKitMetadata = FirebaseVisionImageMetadata.Builder()
    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
    .setWidth(WIDTH)
    .setHeight(HEIGHT)
    .setRotation(3)
    .build()

fun checkZoneFace(face: FirebaseVisionFace?, heightCamera: Int, widthCamera: Int): Boolean {
    face ?: return false
    val centerX = face.boundingBox.centerX()
    val centerY = face.boundingBox.centerY()
    val ya = (heightCamera / 2.5).toInt()
    val yb = heightCamera - (heightCamera / 3)
    val xa = widthCamera / 3
    val xb = widthCamera - (widthCamera / 3)
    val inZone = centerX in xa..xb && centerY in ya..yb
    Log.e(
        "CheckZoneFace",
        "X: $centerX - Y: $centerY - Zone: $xa .. $xb $ya .. $yb - InZone: $inZone"
    )
    return inZone
}

fun List<FirebaseVisionFace>?.getLargestFace(): FirebaseVisionFace? {
    this ?: return null
    var largest: FirebaseVisionFace? = null
    return if (this.isNotEmpty()) {
        this.forEach {
            if (largest == null) {
                largest = it
            } else if (largest!!.boundingBox.width() < largest!!.boundingBox.width()) {
                largest = it
            }
        }
        largest
    } else {
        largest
    }
}

fun FirebaseVisionFace?.checkFaceOk(): Boolean {
    this ?: return false
    return this.headEulerAngleY in FRONT_FACE_VALUE && this.headEulerAngleZ in FRONT_FACE_VALUE
}

fun FirebaseVisionFace?.checkEye(): Boolean {
    this ?: return false
    val eyeLeft =
        this.getLandmark(Landmark.LEFT_EYE)?.position ?: return false
    val eyeRight = this.getLandmark(Landmark.RIGHT_EYE)?.position ?: return false
    log.d("eyeLeft ${eyeLeft.x} ${eyeLeft.y}")
    log.d("eyeRight ${eyeRight.x} ${eyeRight.y}")
    val checkY = eyeLeft.y in 400f..498f && eyeRight.y in 400f..498f
    val checkX = eyeLeft.x <= 376f && eyeRight.x >= 380f
    return checkY && checkX
}

/**
 * get data point to face
 */
fun Bitmap?.getDataFaceAndFace(face: FirebaseVisionFace): DataGetFacePoint {
    this ?: return DataGetFacePoint(null, null)

    val rect = face.boundingBox

    val extraH = 0.5f
    val extraW = 0.5f

    val plusH = rect.height() * extraH
    val plusW = rect.width() * extraW

    val height = rect.height() + plusH.roundToInt()
    val width = rect.width() + plusW.roundToInt()
    val top = rect.top - (plusH / 2).roundToInt()
    val left = rect.left - (plusW / 2).roundToInt()

    val copiedBitmap = this.copy(Bitmap.Config.RGB_565, true)
    try {
        val bmFace = Bitmap.createBitmap(
            copiedBitmap,
            left,
            top,
            width,
            height
        )
        val facePointData = getDataFaceCrop(face, (rect.width() * 0.5f).toInt())

        val byteFace = bmFace.bitmapToByteArray()
        this.recycle()
        bmFace?.recycle()
        return DataGetFacePoint(facePointData, byteFace)
    } catch (ex: Exception) {
        val facePointData = getDataFullFace(face)
        val byteFace = this.bitmapToByteArray()
        this.recycle()
        return DataGetFacePoint(facePointData, byteFace)
    }
}

private fun convertPointFaceCrop(
    oldPoint: FirebaseVisionPoint?,
    faceRect: Rect,
    plus: Int
): Point {
    oldPoint ?: return Point(0, 0)
    val old_X = oldPoint.x.roundToInt()
    val old_Y = oldPoint.y.roundToInt()
    val new_X = old_X - faceRect.left + plus / 2
    val new_Y = old_Y - faceRect.top + plus / 2
    return Point(new_X, new_Y)
}

private fun getDataFaceCrop(face: FirebaseVisionFace, plus: Int): FacePointData {
    val leftTop = convertPointFaceCrop(
        FirebaseVisionPoint(
            face.boundingBox.left.toFloat(),
            face.boundingBox.top.toFloat(),
            0f
        ), face.boundingBox, plus
    )
    val rightBottom = convertPointFaceCrop(
        FirebaseVisionPoint(
            face.boundingBox.right.toFloat(),
            face.boundingBox.bottom.toFloat(),
            0f
        ), face.boundingBox, plus
    )

    val newRect = Rect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y)

    val leftEye = convertPointFaceCrop(
        face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)?.position,
        face.boundingBox,
        plus
    )
    val rightEye = convertPointFaceCrop(
        face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)?.position,
        face.boundingBox,
        plus
    )
    val leftMouth = convertPointFaceCrop(
        face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT)?.position,
        face.boundingBox,
        plus
    )
    val rightMouth = convertPointFaceCrop(
        face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT)?.position,
        face.boundingBox,
        plus
    )
    val nose = convertPointFaceCrop(
        face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)?.position,
        face.boundingBox,
        plus
    )
    return FacePointData(newRect, rightEye, leftEye, nose, rightMouth, leftMouth)
}

private fun getDataFullFace(face: FirebaseVisionFace): FacePointData {
    val leftTop = convertPointFullFrame(
        FirebaseVisionPoint(
            face.boundingBox.left.toFloat(),
            face.boundingBox.top.toFloat(),
            0f
        )
    )
    val rightBottom = convertPointFullFrame(
        FirebaseVisionPoint(
            face.boundingBox.right.toFloat(),
            face.boundingBox.bottom.toFloat(),
            0f
        )
    )

    val newRect = Rect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y)

    val leftEye = convertPointFullFrame(
        face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)?.position
    )
    val rightEye = convertPointFullFrame(
        face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)?.position
    )
    val leftMouth = convertPointFullFrame(
        face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT)?.position
    )
    val rightMouth = convertPointFullFrame(
        face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT)?.position
    )
    val nose = convertPointFullFrame(
        face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)?.position
    )
    return FacePointData(newRect, rightEye, leftEye, nose, rightMouth, leftMouth)
}

private fun convertPointFullFrame(oldPoint: FirebaseVisionPoint?): Point {
    oldPoint ?: return Point(0, 0)
    val old_X = oldPoint.x.roundToInt()
    val old_Y = oldPoint.y.roundToInt()
    return Point(old_X, old_Y)
}

fun checkLandMark(face: FirebaseVisionFace?): Boolean {
    face ?: return false
    return face.headEulerAngleY in FRONT_FACE_VALUE && face.headEulerAngleZ in FRONT_FACE_VALUE
}

fun cropBitmapWithRect(bitmap: Bitmap, rect: Rect): Bitmap? {
    return try {
        val cropBitmap = Bitmap.createBitmap(
            bitmap,
            rect.left, rect.top, rect.width(), rect.height()
        )
        cropBitmap
    } catch (ex: Exception) {
        Log.e("getFaceResult", ex.message.toString())
        null
    }
}


fun Classifier.Recognition?.checkTitleMaskFace(): Boolean {
    try {
        this ?: return false
        val confidence = this.confidence
        val checkTitle = this.title.trim() == "no mask" || this.title.trim() == "no-mask"
        return confidence >= 0.6f && checkTitle
    } catch (e: java.lang.Exception) {
        return false
    }
}