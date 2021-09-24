package com.kt.myproject.ui.fragment.camera

import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.camera.DataGetFacePoint
import com.kt.myproject.camera.DetectFace
import com.kt.myproject.camera.FMDetector
import com.kt.myproject.camera.cropBitmapWithRect
import com.kt.myproject.databinding.FragmentMaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wee.digital.alfar.utils.camera.toBitmap

class Mask2Fragment : BaseFragment<FragmentMaskBinding>(FragmentMaskBinding::inflate),
    DetectFace.DetectCallBack {

    private var job: Job? = null

    private var mask2Detect: FMDetector? = null

    private var processing = false

    override fun onViewCreated() {
        mask2Detect = FMDetector(requireContext())

        binding.maskCameraPreview.startCamera(this)
        binding.maskCameraPreview.initEventDetection(this)
    }

    override fun onLiveDataObserve() {
    }

    /**
     * module detection
     */
    @SuppressLint("SetTextI18n")
    override fun faceEligible(face: FirebaseVisionFace, dataFace: DataGetFacePoint) {
        if (processing) return
        processing = true
        val time = System.currentTimeMillis()
        val bitmap = dataFace.fullFrame.toBitmap()?.let { cropBitmapWithRect(it, face.boundingBox) }
        var text = ""
        text = when (bitmap == null) {
            true -> "bitmap null"
            else -> {
                val check = mask2Detect?.isWithMask(bitmap) ?: true
                if (check) "face mask" else "face not mask"
            }
        }
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.Main) {
            binding.maskLabelStatus.text = text + "${System.currentTimeMillis() - time}"
        }
        processing = false
    }

    override fun faceNotFrame() {
        super.faceNotFrame()
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.Main) {
            binding.maskLabelStatus.text = ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mask2Detect?.destroy()
        binding.maskCameraPreview.onPauseCamera()
        binding.maskCameraPreview.onDestroyCamera()
    }

}