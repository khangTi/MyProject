package com.kt.myproject.ex

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import kotlin.math.roundToInt

fun Activity.getRootView(): View {
    return findViewById(android.R.id.content)
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics
    )
}

fun Activity.keyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = this.convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError
}

fun Activity.keyboardClosed(): Boolean {
    return !this.keyboardOpen()
}


fun Activity?.hideKeyboard() {
    this ?: return
    var view = currentFocus
    if (view == null) view = View(this)
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity?.showKeyboard() {
    this ?: return
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Activity?.hideSystemUI() {
    this?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    hideKeyboard()
    hideStatusBar()
    hideNavigationBar()
}

fun Activity?.hideStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity?.hideNavigationBar(hasFocus: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && hasFocus) this?.window?.apply {
        setDecorFitsSystemWindows(false)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val decorView = this?.window?.decorView ?: return
        decorView.systemUiVisibility = flags
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = flags
            }
        }
    }
}

fun Activity?.statusBarColor(@ColorInt _color: Int?) {
    this ?: return
    val color = _color ?: Color.TRANSPARENT
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
    if (color.isDarkColor()) darkStatusBar() else lightStatusBar()
}

fun Activity?.statusBarColorRes(@ColorRes colorRes: Int) {
    this?.statusBarColor(color(colorRes))
}

fun Activity?.lightStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = 8192
    }
}

fun Activity?.darkStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = 0
    }
}

fun Activity?.adjustResize() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this?.window?.setDecorFitsSystemWindows(false)
    } else {
        @Suppress("DEPRECATION")
        this?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}

fun Activity?.adjustNothing() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this?.window?.setDecorFitsSystemWindows(true)
    }
    this?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}






