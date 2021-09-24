package com.kt.myproject.ui.fragment.camera

import androidx.lifecycle.lifecycleScope
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.camera.DataGetFacePoint
import com.kt.myproject.camera.DetectFace
import com.kt.myproject.camera.MaskDetection
import com.kt.myproject.databinding.FragmentMaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MaskFragment : BaseFragment<FragmentMaskBinding>(FragmentMaskBinding::inflate),
    MaskDetection.MaskEvent, DetectFace.DetectCallBack {

    private var maskDetect: MaskDetection? = null

    private var job: Job? = null

    override fun onViewCreated() {
        maskDetect = MaskDetection(requireContext())
        maskDetect?.event = this

        binding.maskCameraPreview.startCamera(this)
        binding.maskCameraPreview.initEventDetection(this)
    }

    override fun onLiveDataObserve() {
    }

    /**
     * module mask
     */
    override fun onFaceMask(bool: Boolean, confidence: Float, time: Long) {
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.Main) {
            binding.maskLabelStatus.text =
                if (bool) "face not mask $confidence - $time" else "face mask $confidence - $time"
        }
    }

    /**
     * module detection
     */
    override fun faceEligible(face: FirebaseVisionFace, dataFace: DataGetFacePoint) {
        maskDetect?.detectMask(dataFace.fullFrame!!, face)
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
        binding.maskCameraPreview.onPauseCamera()
        binding.maskCameraPreview.onDestroyCamera()
        maskDetect?.onDestroy()
    }

}