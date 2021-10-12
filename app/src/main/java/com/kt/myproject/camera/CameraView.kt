package com.kt.myproject.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.CameraViewBinding
import com.kt.myproject.ex.toast
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraView(context: Context, attrs: AttributeSet) :
    AppBindCustomView<CameraViewBinding>(context, attrs, CameraViewBinding::inflate) {

    private var isDetect = true

    private var displayId: Int = -1

    private var camera: Camera? = null

    private var preview: Preview? = null

    private var imageCapture: ImageCapture? = null

    private var imageAnalyzer: ImageAnalysis? = null

    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT

    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var cameraExecutor: ExecutorService

    /**
     *
     */
    var resultEvent: FrameResult = { _, _, _ -> }

    private var detect: DetectFace? = null


    override fun onViewInit(context: Context, types: TypedArray) {
    }

    fun startCamera(lifecycle: LifecycleOwner) {
        detect = DetectFace(context)
        cameraExecutor = Executors.newScheduledThreadPool(4)
        binding.cameraView.post {
            displayId = binding.cameraView.display.displayId
            setupCamera(lifecycle)
        }
    }

    private fun setupCamera(lifecycle: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            if (CameraSelector.LENS_FACING_BACK == lensFacing && !cameraProvider.cameraBack()) {
                toast("wee.digital.alfar.utils.camera not found")
                return@Runnable
            }
            if (CameraSelector.LENS_FACING_FRONT == lensFacing && !cameraProvider.cameraFront()) {
                toast("wee.digital.alfar.utils.camera not found")
                return@Runnable
            }
            bindCameraUseCases(lifecycle)
        }, ContextCompat.getMainExecutor(context))
    }

    @SuppressLint("RestrictedApi")
    private fun bindCameraUseCases(lifecycle: LifecycleOwner) {
        val metrics = DisplayMetrics().also { binding.cameraView.display?.getRealMetrics(it) }
        val screenAspectRatio = metrics.aspectRatio()
        val rotation = binding.cameraView.display.rotation
        val cameraProvider =
            cameraProvider ?: throw java.lang.IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .setMaxResolution(SIZE)
            .setDefaultResolution(SIZE)
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetAspectRatio(screenAspectRatio)
            .setDefaultResolution(SIZE)
            .setMaxResolution(SIZE)
            .setTargetRotation(rotation)
            .build()
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .setDefaultResolution(SIZE)
            .setMaxResolution(SIZE)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { frame, width, height ->
                    resultEvent(frame, width, height)
                    if (isDetect) detect?.mlKitDetectFrame(frame)
                })
            }

        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(
                lifecycle,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(binding.cameraView.surfaceProvider)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * capture image
     */
    fun captureImage() = callbackFlow {
        val time = System.currentTimeMillis()
        val listenerCapture = object : ImageCapture.OnImageCapturedCallback() {
            override fun onError(exception: ImageCaptureException) {
                close(exception)
            }

            @SuppressLint("UnsafeOptInUsageError")
            override fun onCaptureSuccess(image: ImageProxy) {
                toast("${System.currentTimeMillis() - time}")
                val rotation = image.imageInfo.rotationDegrees
                val bitmap = image.image?.toBitmap().rotate(rotation)
                if (bitmap == null) {
                    close(Exception("bitmap null"))
                } else {
                    offer(bitmap)
                }
                image.close()
            }
        }
        imageCapture?.takePicture(cameraExecutor, listenerCapture)
        awaitClose { cancel() }
    }

    /**
     * lifeCycle camera
     */
    fun pausePreview() {
        isDetect = false
        cameraProvider?.unbind(preview)
    }

    fun startDetect() {
        isDetect = true
    }

    fun initEventDetection(event: DetectFace.DetectCallBack) {
        detect?.detectEvent = event
    }

    fun onPauseCamera() {
        detect?.destroyDetection()
    }

    fun onDestroyCamera(){
        cameraExecutor.shutdown()
    }

}