package com.kt.myproject.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.kt.myproject.R
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.WidgetInputMessageBinding
import com.kt.myproject.ex.SimpleTextWatcher
import com.kt.myproject.ex.post
import com.kt.myproject.ui.fragment.gallery.keyboard.KeyboardAwareLinearLayout

class WidgetChatInput(context: Context, attrs: AttributeSet?) :
    AppBindCustomView<WidgetInputMessageBinding>(
        context, attrs, WidgetInputMessageBinding::inflate
    ), SimpleTextWatcher, KeyboardAwareLinearLayout.OnKeyboardShownListener {

    var listener: WidgetChatInputListener? = null

    var text: String
        get() {
            val t = bd.chatInputInput.text.toString()
            return if (t.isEmpty()) "\uD83D\uDC4D"/*like*/ else t
        }
        set(value) {
            bd.chatInputInput.setText(value)
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewInit(context: Context, types: TypedArray) {
        setupStubGaller()
        bd.chatInputInput.addTextChangedListener(this)
        bd.chatInputAudio.setOnClickListener {
            listener?.onMicClick()
        }
        bd.chatInputEmoji.setOnClickListener {
            hideGallery()
            bd.widgetInputGallery.show(bd.chatInputInput, bd.emojiView)
            listener?.onEmojiClick()
        }
        bd.chatInputSend.setOnClickListener {
            listener?.onSendClick(text)
        }
        bd.chatInputPhoto.setOnClickListener {
            hideEmoji()
            bd.widgetInputGallery.show(bd.chatInputInput, bd.chatInputStub)
            bd.chatInputStub.bindAdapterView()
        }
        bd.chatInputInput.setOnTouchListener { _, _ ->
            animViewFocus()
            false
        }
        bd.chatInputInput.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> animViewFocus()
                else -> animViewUnFocus()
            }
        }
        bd.chatInputAdd.setOnClickListener {
            when (iconAdd) {
                true -> listener?.onAddClick()
                else -> animViewUnFocus()
            }
        }
    }

    private fun setupStubGaller() {
        bd.widgetInputGallery.setIsBubble(false)
        bd.widgetInputGallery.addOnKeyboardShownListener(this)
        bd.widgetInputGallery.hideAttachedInput(false)
        bd.widgetInputGallery.show(bd.chatInputInput, bd.chatInputStub)
        post(100) {
            hideEmoji()
            bd.widgetInputGallery.hideAttachedInput(true)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        when (s.toString().isEmpty()) {
            true -> bd.chatInputSend.setImageResource(R.drawable.ic_like)
            else -> bd.chatInputSend.setImageResource(R.drawable.ic_send)
        }
    }

    /**
     * config animation
     */
    private var iconAdd = true

    private fun animViewFocus() {
        iconAdd = false
        val view = bd.chatInputBg.id
        val viewAdd = bd.chatInputAdd.id
        ChangeBounds().apply { duration = 250 }.beginTransition(bd.chatInputParentView) {
            this.clear(bd.chatInputBg.id, ConstraintSet.START)
            connect(view, ConstraintSet.START, viewAdd, ConstraintSet.END)
        }
        bd.chatInputAdd.setImageResource(R.drawable.ic_next)
    }

    private fun animViewUnFocus() {
        iconAdd = true
        val view = bd.chatInputBg.id
        val viewAudio = bd.chatInputAudio.id
        ChangeBounds().apply { duration = 250 }.beginTransition(bd.chatInputParentView) {
            this.clear(view, ConstraintSet.START)
            connect(view, ConstraintSet.START, viewAudio, ConstraintSet.END)
        }
        bd.chatInputAdd.setImageResource(R.drawable.ic_plus)
    }

    interface WidgetChatInputListener {
        fun onAddClick() {}
        fun onMicClick() {}
        fun onPhotoClick() {}
        fun onEmojiClick() {}
        fun onSendClick(mess: String, typeData: String? = null) {}
    }

    fun Transition.beginTransition(
        layout: ConstraintLayout,
        block: ConstraintSet.() -> Unit
    ): Transition {
        TransitionManager.beginDelayedTransition(layout, this)
        layout.editConstraint(block)
        return this
    }

    fun ConstraintLayout.editConstraint(block: ConstraintSet.() -> Unit) {
        ConstraintSet().also {
            it.clone(this)
            it.block()
            it.applyTo(this)
        }
    }

    override fun onKeyboardShown() {
        bd.widgetInputGallery.hideAttachedInput(true)
    }

    private fun hideGallery() {
        if (bd.chatInputStub.isShowing()) {
            bd.chatInputStub.hide(true)
        }
    }

    private fun hideEmoji() {
        if (bd.emojiView.isShowing()) {
            bd.emojiView.hide(true)
        }
    }

}