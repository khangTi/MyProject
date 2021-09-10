package com.kt.myproject.ex

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText

private var lastClickTime: Long = 0

private var lastClickViewId: Int = -1

abstract class ViewClickListener(private val delayedInterval: Long = 300) : View.OnClickListener {

    abstract fun onClicks(v: View?)

    private val View?.isAcceptClick: Boolean get() = this?.id != lastClickViewId && delayedInterval == 0L

    private val isDelayed: Boolean get() = nowInMillis - lastClickTime > delayedInterval

    final override fun onClick(v: View?) {
        Log.d("ViewClickLtn", getResName(v))
        val b1 = isDelayed
        val b2 = v.isAcceptClick
        if (b1 || b2) {
            Log.d("ViewClickLtn", "hasBeenClick $b1")
            Log.d("ViewClickLtn", "v.isAcceptClick $b2")
            lastClickViewId = v?.id ?: -1
            lastClickTime = 0
            onClicks(v)
        }
        lastClickTime = nowInMillis
    }

}

fun View.addViewClickListener(delayedInterval: Long = 700, block: (View?) -> Unit) {
    setOnClickListener(object : ViewClickListener(delayedInterval) {
        override fun onClicks(v: View?) {
            block(v)
        }
    })
}

fun View?.addViewClickListener(listener: View.OnClickListener?) {
    this ?: return
    if (this is EditText && listener != null) {
        isFocusable = false
        isCursorVisible = false
        keyListener = null
        inputType = EditorInfo.IME_ACTION_NONE
    }
    setOnClickListener(listener)
}

fun addClickListeners(vararg views: View?, block: (View?) -> Unit) {
    val listener = object : ViewClickListener() {
        override fun onClicks(v: View?) {
            block(v)
        }
    }
    views.forEach {
        it?.setOnClickListener(listener)
    }
}

fun clearClickListeners(vararg views: View?) {
    views.forEach {
        it?.setOnClickListener(null)
    }
}

abstract class FastClickListener(private val clickCount: Int) : View.OnClickListener {

    private var lastClickTime: Long = 0

    private var currentClickCount: Int = 0

    abstract fun onViewClick(v: View?)

    final override fun onClick(v: View?) {
        if (nowInMillis - lastClickTime > 420 || currentClickCount >= clickCount) {
            currentClickCount = 0
        }
        lastClickTime = nowInMillis
        currentClickCount++
        if (currentClickCount == clickCount) {
            lastClickTime = 0
            currentClickCount = 0
            onViewClick(v)
        }
    }

}

fun View?.addFastClickListener(clickCount: Int, block: () -> Unit) {
    this?.setOnClickListener(object : FastClickListener(clickCount) {
        override fun onViewClick(v: View?) {
            block()
        }
    })
}
