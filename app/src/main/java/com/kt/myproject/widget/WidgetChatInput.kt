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
            val t = binding.chatInputInput.text.toString()
            return if (t.isEmpty()) "\uD83D\uDC4D"/*like*/ else t
        }
        set(value) {
            binding.chatInputInput.setText(value)
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewInit(context: Context, types: TypedArray) {
        setupStubGaller()
        binding.chatInputInput.addTextChangedListener(this)
        binding.chatInputAudio.setOnClickListener {
            listener?.onMicClick()
        }
        binding.chatInputEmoji.setOnClickListener {
            hideGallery()
            binding.widgetInputGallery.show(binding.chatInputInput, binding.emojiView)
            listener?.onEmojiClick()
        }
        binding.chatInputSend.setOnClickListener {
            listener?.onSendClick(text)
        }
        binding.chatInputPhoto.setOnClickListener {
            hideEmoji()
            binding.widgetInputGallery.show(binding.chatInputInput, binding.chatInputStub)
            binding.chatInputStub.bindAdapterView()
        }
        binding.chatInputInput.setOnTouchListener { _, _ ->
            animViewFocus()
            false
        }
        binding.chatInputInput.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> animViewFocus()
                else -> animViewUnFocus()
            }
        }
        binding.chatInputAdd.setOnClickListener {
            when (iconAdd) {
                true -> listener?.onAddClick()
                else -> animViewUnFocus()
            }
        }
    }

    private fun setupStubGaller() {
        binding.widgetInputGallery.setIsBubble(false)
        binding.widgetInputGallery.addOnKeyboardShownListener(this)
        binding.widgetInputGallery.hideAttachedInput(false)
        binding.widgetInputGallery.show(binding.chatInputInput, binding.chatInputStub)
        post(100) {
            hideEmoji()
            binding.widgetInputGallery.hideAttachedInput(true)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        when (s.toString().isEmpty()) {
            true -> binding.chatInputSend.setImageResource(R.drawable.ic_like)
            else -> binding.chatInputSend.setImageResource(R.drawable.ic_send)
        }
    }

    /**
     * config animation
     */
    private var iconAdd = true

    private fun animViewFocus() {
        iconAdd = false
        val view = binding.chatInputBg.id
        val viewAdd = binding.chatInputAdd.id
        ChangeBounds().apply { duration = 250 }.beginTransition(binding.chatInputParentView) {
            this.clear(binding.chatInputBg.id, ConstraintSet.START)
            connect(view, ConstraintSet.START, viewAdd, ConstraintSet.END)
        }
        binding.chatInputAdd.setImageResource(R.drawable.ic_next)
    }

    private fun animViewUnFocus() {
        iconAdd = true
        val view = binding.chatInputBg.id
        val viewAudio = binding.chatInputAudio.id
        ChangeBounds().apply { duration = 250 }.beginTransition(binding.chatInputParentView) {
            this.clear(view, ConstraintSet.START)
            connect(view, ConstraintSet.START, viewAudio, ConstraintSet.END)
        }
        binding.chatInputAdd.setImageResource(R.drawable.ic_plus)
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
        binding.widgetInputGallery.hideAttachedInput(true)
    }

    private fun hideGallery() {
        if (binding.chatInputStub.isShowing()) {
            binding.chatInputStub.hide(true)
        }
    }

    private fun hideEmoji() {
        if (binding.emojiView.isShowing()) {
            binding.emojiView.hide(true)
        }
    }

}