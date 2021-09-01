package com.kt.myproject.ex

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionLayout

interface SimpleTextWatcher : TextWatcher {

    fun EditText.setSilentText(s: String) {
        removeTextChangedListener(this@SimpleTextWatcher)
        setText(s)
        setSelection(s.length)
        addTextChangedListener(this@SimpleTextWatcher)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textChange(s.toString())
    }

    fun textChange(string: String) {}

}

interface SimpleMotionTransitionListener : MotionLayout.TransitionListener {
    override fun onTransitionChange(layout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
    }

    override fun onTransitionStarted(layout: MotionLayout?, startId: Int, endId: Int) {
    }

    override fun onTransitionCompleted(layout: MotionLayout?, currentId: Int) {
    }

    override fun onTransitionTrigger(layout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
    }
}