package com.kt.myproject.ui.fragment.adv

class AdvItem(
        val videoRes: Int? = null,
        val imageRes: Int? = null
) {
    var videoController: VideoControllerDelegate? = null

    val isImage get() = imageRes != null

    val isVideo get() = videoRes != null
}