package com.kt.myproject.ui.fragment.call

import android.content.Intent
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.FragmentCallBinding
import com.kt.myproject.rtc.AppRTCAudioManager
import com.kt.myproject.rtc.AppRTCClient
import com.kt.myproject.rtc.PeerConnectionClient
import com.kt.myproject.rtc.UnhandledExceptionHandler
import org.webrtc.*

class CallFragment : BaseFragment<FragmentCallBinding>(FragmentCallBinding::inflate) {

    private val statCallBackPeriod = 1000

    private val remoteProxyRenderer: ProxyVideoSink = ProxyVideoSink()
    private val localProxyVideoSink: ProxyVideoSink = ProxyVideoSink()

    private var peerConnectionClient: PeerConnectionClient? = null

    private var appRtcClient: AppRTCClient? = null

    private var signalingParameters: AppRTCClient.SignalingParameters? = null

    private var audioManager: AppRTCAudioManager? = null

    private var pipRenderer: SurfaceViewRenderer? = null
    private var fullScreenRenderer: SurfaceViewRenderer? = null

    private var videoFileRenderer: VideoFileRenderer? = null

    private var roomConnectionParameters: AppRTCClient.RoomConnectionParameters? = null
    private var peerConnectionParameters: PeerConnectionClient.PeerConnectionParameters? = null

    private var mediaProjectionPermissionResultData: Intent? = null

    private var connected: Boolean = false

    private var isError: Boolean = false

    private var callStartedTimeMs: Boolean = false

    private var micEnable: Boolean = false

    private var screenCaptureEnable: Boolean = false

    private var isSwappedFeeds: Boolean = false

    private var commandLineRun: Boolean = false

    private var activityRunning: Boolean = false

    private var remoteSinks = arrayListOf<VideoSink>()

    private var mediaProjectionPermissionResultCode: Int = -1

    override fun onViewCreated() {
        Thread.setDefaultUncaughtExceptionHandler(UnhandledExceptionHandler(requireActivity()))

        remoteSinks.add(remoteProxyRenderer)

        val eglBase = EglBase.create()
        pipRenderer?.init(eglBase.eglBaseContext, null)
        pipRenderer?.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)



    }

    override fun onLiveDataObserve() {}

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
        fun setTarget(target: VideoSink) {
            this.target = target
        }
    }

}