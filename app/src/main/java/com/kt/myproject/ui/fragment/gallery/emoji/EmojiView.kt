package com.kt.myproject.ui.fragment.gallery.emoji

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.EmojiViewBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.ui.fragment.gallery.keyboard.InputAwareLayout

class EmojiView(context: Context, attrs: AttributeSet) :
    AppBindCustomView<EmojiViewBinding>(context, attrs, EmojiViewBinding::inflate),
    InputAwareLayout.InputView {

    override fun onViewInit(context: Context, types: TypedArray) {
        bd.emojiViewEmoji.setOnClickListener { toast("emoji") }
        bd.emojiViewSticker.setOnClickListener { toast("sticker") }
        bd.emojiViewGif.setOnClickListener { toast("gif") }
    }

    override fun show(height: Int, immediate: Boolean) {
        val params = layoutParams
        params.height = height
        layoutParams = params
        visibility = VISIBLE
    }

    override fun hide(immediate: Boolean) {
        visibility = GONE
    }

    override fun isShowing(): Boolean {
        return visibility == VISIBLE
    }

}