package com.kt.myproject.ui.fragment.gallery.keyboard

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes

fun View.statusBarHeight(): Int {
    val id = resources.getIdentifier("status_bar_height", "dimen", "android")
    return when (id > 0) {
        true -> resources.getDimensionPixelSize(id)
        else -> 0
    }
}

fun Context.windowManager(): WindowManager {
    return getSystemService(Activity.WINDOW_SERVICE) as WindowManager
}

fun Context.inputMethodManager(): InputMethodManager {
    return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}

fun clamp(value: Int, min: Int, max: Int): Int {
    return Math.min(Math.max(value, min), max)
}

fun <T : View?> findStubById(parent: Activity, @IdRes resId: Int): Stub<T>? {
    return Stub(parent.findViewById(resId))
}