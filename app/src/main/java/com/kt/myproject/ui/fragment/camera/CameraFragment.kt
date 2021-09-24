package com.kt.myproject.ui.fragment.camera

import androidx.lifecycle.lifecycleScope
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.kt.myproject.R
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.camera.DataGetFacePoint
import com.kt.myproject.camera.DetectFace
import com.kt.myproject.databinding.FragmentCameraBinding
import com.kt.myproject.ex.color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import wee.digital.alfar.utils.camera.toBitmap

class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate),
    DetectFace.DetectCallBack {

    private var job: Job? = null

    override fun onViewCreated() {
        binding.cameraPreview.startCamera(this)
        binding.cameraPreview.initEventDetection(this)
    }

    override fun onLiveDataObserve() {

    }

    override fun onPermissionGranted(granted: String) {

    }

    override fun faceOnFrame() {
        super.faceOnFrame()
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.Main) {
            binding.cameraLabelStatus.text = "Khuôn mặt trong vùng nhận diện"
            binding.cameraLabelStatus.setTextColor(color(R.color.teal_200))
        }
    }

    override fun faceNotFrame() {
        super.faceNotFrame()
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.Main) {
            binding.cameraLabelStatus.text = "Không tìm thấy khuôn mặt"
            binding.cameraLabelStatus.setTextColor(color(R.color.red))
        }
    }

    override fun faceEligible(face: FirebaseVisionFace, dataFace: DataGetFacePoint) {
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.Main) {
            binding.cameraFrameResult.setImageBitmap(dataFace.face?.toBitmap())
        }
    }

    override fun onPause() {
        super.onPause()
        binding.cameraPreview.onPauseCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cameraPreview.onDestroyCamera()
    }

}