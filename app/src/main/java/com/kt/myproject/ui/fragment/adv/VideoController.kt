package com.kt.myproject.ui.fragment.adv

import android.net.Uri
import androidx.annotation.RawRes
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.kt.myproject.app.app

class VideoController(
        private val playerView: PlayerView,
        @RawRes val videoRes: Int
) : Player.EventListener, VideoControllerDelegate {

    init {
    }

    private val videoUri: String = RawResourceDataSource.buildRawResourceUri(videoRes).toString()

    private var simpleExoplayer: SimpleExoPlayer? = null

    var onVideoStarted: () -> Unit = {}

    var onVideoStopped: () -> Unit = {}

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(app, "exoplayer-simple")
    }

    private fun buildMediaSource(videoUrl: String): MediaSource {
        return ProgressiveMediaSource.Factory(
                dataSourceFactory
        ).createMediaSource(
                MediaItem.fromUri(Uri.parse(videoUrl))
        )
    }

    override fun onPlaybackStateChanged(state: Int) {
        when (state) {
            Player.STATE_IDLE -> {

            }
            Player.STATE_BUFFERING -> {

            }
            Player.STATE_READY -> onVideoStarted()
            Player.STATE_ENDED -> onVideoStopped()
        }
    }

    /**
     * [] implement
     */
    override fun playVideo() {
        if (simpleExoplayer != null) {
            stopVideo()
        }
        simpleExoplayer = SimpleExoPlayer.Builder(app).build().also {
            val mediaSource = buildMediaSource(videoUri)
            it.setMediaSource(mediaSource, true)
            playerView.player = it
            it.seekTo(1)
            it.playWhenReady = true
            it.addListener(this)
            it.prepare()
        }

    }

    override fun stopVideo() {
        if (simpleExoplayer == null) return
        simpleExoplayer?.also {
            it.removeListener(this)
            it.stop()
            it.release()
            simpleExoplayer = null
        }
    }

}



