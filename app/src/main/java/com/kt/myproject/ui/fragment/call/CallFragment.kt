package com.kt.myproject.ui.fragment.call

import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.WindowManager
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.base.activityVM
import com.kt.myproject.databinding.FragmentCallBinding
import com.kt.myproject.rtc.*
import org.webrtc.*

class CallFragment : BaseFragment<FragmentCallBinding>(FragmentCallBinding::inflate),
    AppRTCClient.SignalingEvents {

    private val vm by lazy { activityVM(CallVM::class) }

    private val statCallBackPeriod = 1000

    private val remoteProxyRenderer: ProxyVideoSink = ProxyVideoSink()
    private val localProxyVideoSink: ProxyVideoSink = ProxyVideoSink()

    private var peerConnectionClient: PeerConnectionClient? = null

    private var appRtcClient: AppRTCClient? = null

    private var signalingParameters: AppRTCClient.SignalingParameters? = null

    private var audioManager: AppRTCAudioManager? = null

    private var videoFileRenderer: VideoFileRenderer? = null

    private var roomConnectionParameters: AppRTCClient.RoomConnectionParameters? = null
    private var peerConnectionParameters: PeerConnectionClient.PeerConnectionParameters? = null

    private var mediaProjectionPermissionResultData: Intent? = null

    private var connected: Boolean = false

    private var isError: Boolean = false

    private var callStartedTimeMs: Long = 0L

    private var micEnable: Boolean = false

    private var screenCaptureEnable: Boolean = false

    private var isSwappedFeeds: Boolean = false

    private var commandLineRun: Boolean = false

    private var activityRunning: Boolean = false

    private var remoteSinks = arrayListOf<VideoSink>()

    private var mediaProjectionPermissionResultCode: Int = -1

    private val roomUri = "https://appr.tc"

    private val roomId = vm.idCallSingle.value ?: ""

    private var cpuMonitor: CpuMonitor? = null

    override fun onViewCreated() {
        Thread.setDefaultUncaughtExceptionHandler(UnhandledExceptionHandler(requireActivity()))
        remoteSinks.add(remoteProxyRenderer)

        val eglBase = EglBase.create()
        binding.callLocal.init(eglBase.eglBaseContext, null)
        binding.callLocal.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)

        binding.callRemote.init(eglBase.eglBaseContext, null)
        binding.callRemote.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)

        binding.callLocal.setZOrderMediaOverlay(true)
        binding.callLocal.setEnableHardwareScaler(true)
        binding.callRemote.setEnableHardwareScaler(false)

        setSwappedFeeds(true)
        var loopback = false
        var tracing = false
        val dataChannelParameters = PeerConnectionClient.DataChannelParameters(
            true,
            -1,
            -1,
            "",
            false,
            -1
        )
        peerConnectionParameters = PeerConnectionClient.PeerConnectionParameters(
            true,
            false,
            false,
            0,
            0,
            0,
            0,
            "VP8",
            true,
            false,
            0,
            "OPUS",
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            dataChannelParameters
        )
        commandLineRun = false
        val intTimeMs = 0

        appRtcClient = if (loopback || !DirectRTCClient.IP_PATTERN.matcher(roomId).matches()) {
            WebSocketRTCClient(this)
        } else {
            DirectRTCClient(this)
        }
        roomConnectionParameters =
            AppRTCClient.RoomConnectionParameters(roomUri, roomId, loopback, null)

        if (CpuMonitor.isSupported()) {
            cpuMonitor = CpuMonitor(requireContext())
        }

    }

    override fun onLiveDataObserve() {}

    private fun setSwappedFeeds(b: Boolean) {
        this.isSwappedFeeds = b
        val proxyLocal = if (b) binding.callRemote else binding.callLocal
        localProxyVideoSink.setTarget(proxyLocal)
        val proxyRemote = if (b) binding.callLocal else binding.callRemote
        remoteProxyRenderer.setTarget(proxyRemote)
        binding.callRemote.setMirror(b)
        binding.callLocal.setMirror(b)
    }

    private fun getDisplayMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics
    }

    private fun getSystemUiVisibility(): Int {
        return SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun useCamera2(): Boolean {
        return Camera2Enumerator.isSupported(requireContext())
    }

    private fun captureToTexture(): Boolean {
        return true
    }

    private fun createCameraCapture(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames
        for (model in deviceNames) {
            if (enumerator.isFrontFacing(model)) {
                val videoCapture = enumerator.createCapturer(model, null)
                if (videoCapture != null) {
                    return videoCapture
                }
            }
        }

        for (model in deviceNames) {
            if (!enumerator.isFrontFacing(model)) {
                val videoCapture = enumerator.createCapturer(model, null)
                if (videoCapture != null) {
                    return videoCapture
                }
            }
        }

        return null
    }

    override fun onStop() {
        super.onStop()
        activityRunning = false
        peerConnectionClient?.stopVideoSource()
        cpuMonitor?.pause()
    }

    override fun onStart() {
        super.onStart()
        activityRunning = true
        peerConnectionClient?.startVideoSource()
        cpuMonitor?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Thread.setDefaultUncaughtExceptionHandler(null)
        activityRunning = false
        disconnect()
    }

    private inner class ProxyVideoSink : VideoSink {
        private var target: VideoSink? = null

        override fun onFrame(p0: VideoFrame?) {
            if (target == null) {
                log.d("Dropping frame in proxy because target is null")
                return
            }
            target?.onFrame(p0)
        }

        @Synchronized
        fun setTarget(target: VideoSink?) {
            this.target = target
        }
    }

    private fun onCallHangUp() {
        disconnect()
    }

    private fun onCameraSwitch() {
        peerConnectionClient?.switchCamera()
    }

    private fun onVideoScalingSwitch(scalingType: RendererCommon.ScalingType) {
        binding.callRemote.setScalingType(scalingType)
    }

    private fun onCaptureFormatChange(width: Int, height: Int, frameRate: Int) {
        peerConnectionClient?.changeCaptureFormat(width, height, frameRate)
    }

    private fun onToggleMic(): Boolean {
        micEnable = !micEnable
        peerConnectionClient?.setAudioEnabled(micEnable)
        return micEnable
    }

    private fun startCall() {
        callStartedTimeMs = System.currentTimeMillis()
        appRtcClient?.connectToRoom(roomConnectionParameters)
        audioManager = AppRTCAudioManager.create(requireContext())
        audioManager?.start { a, b -> onAudioManagerDevicesChanged(a, b) }
    }

    private fun callConnected() {
        peerConnectionClient?.enableStatsEvents(true, 1000)
        setSwappedFeeds(false)
    }

    private fun onAudioManagerDevicesChanged(
        device: AppRTCAudioManager.AudioDevice,
        availableDevices: Set<AppRTCAudioManager.AudioDevice>
    ) {
        log.d("onAudiManagerDevicesChanged $availableDevices selected : $device")
    }

    private fun disconnect() {
        activityRunning = false
        remoteProxyRenderer.setTarget(null)
        localProxyVideoSink.setTarget(null)
        appRtcClient?.disconnectFromRoom()
        appRtcClient = null

        binding.callRemote.release()
        binding.callLocal.release()

        videoFileRenderer?.release()
        videoFileRenderer = null

        peerConnectionClient?.close()
        peerConnectionClient = null

        audioManager?.stop()
        audioManager = null

        navigateUp()
    }

    private fun disconnectWithErrorMessage(){

    }

    /**
     * status rtc
     */
    override fun onConnectedToRoom(params: AppRTCClient.SignalingParameters?) {
    }

    override fun onRemoteDescription(sdp: SessionDescription?) {
    }

    override fun onRemoteIceCandidate(candidate: IceCandidate?) {
    }

    override fun onRemoteIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {
    }

    override fun onChannelClose() {
    }

    override fun onChannelError(description: String?) {
    }

    companion object {
        const val CAPTURE_PERMISSION_REQUEST_CODE = 1
    }

}