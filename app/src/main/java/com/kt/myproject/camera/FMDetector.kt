package com.kt.myproject.camera

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FMDetector(val context: Context) {
    companion object {
        private const val MODEL_FILE = "mask2/model.tflite"
        private const val LABELS = "mask2/labels.txt"
        private const val LABEL_MASK = "mask"
        private const val LABEL_NO_MASK = "no_mask"
    }

    private lateinit var model: Interpreter
    private var labels = listOf<String>()

    init {
        val modelFile = FileUtil.loadMappedFile(context, MODEL_FILE)
        model = Interpreter(modelFile, Interpreter.Options())
        labels = FileUtil.loadLabels(context, LABELS)
    }

    fun destroy() {
        model.close()
    }

    fun isWithMask(input: Bitmap): Boolean {
        return try {
            val imageDataType = model.getInputTensor(0).dataType()
            val inputShape = model.getInputTensor(0).shape()

            val outputDataType = model.getOutputTensor(0).dataType()
            val outputShape = model.getOutputTensor(0).shape()

            var inputImageBuffer = TensorImage(imageDataType)
            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            val cropSize = kotlin.math.min(input.width, input.height)
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(ResizeOp(inputShape[1], inputShape[2], ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(NormalizeOp(127.5f, 127.5f))
                .build()

            inputImageBuffer.load(input)
            inputImageBuffer = imageProcessor.process(inputImageBuffer)

            model.run(inputImageBuffer.buffer, outputBuffer.buffer.rewind())

            val labelOutput = TensorLabel(labels, outputBuffer)
            val outputMap = labelOutput.mapWithFloatValue
            val with = outputMap[LABEL_MASK] ?: 0F
            val without = outputMap[LABEL_NO_MASK] ?: 0F
            with > without
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }

    }
}