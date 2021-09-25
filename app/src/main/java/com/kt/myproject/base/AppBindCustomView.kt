package com.kt.myproject.base

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.kt.myproject.R
import com.kt.myproject.app.app
import com.kt.myproject.ex.Inflate

abstract class AppBindCustomView<VB : ViewBinding>(
    context: Context,
    attrs: AttributeSet?,
    val inflate: Inflate<VB>
) : FrameLayout(context, attrs) {

    var binding: VB

    protected abstract fun onViewInit(context: Context, types: TypedArray)

    open fun initAttrs(context: Context, types: AttributeSet?) {}

    protected open fun styleResource(): IntArray {
        return R.styleable.CustomView
    }

    init {
        val types = context.theme.obtainStyledAttributes(attrs, styleResource(), 0, 0)
        try {
            binding = inflate.invoke(LayoutInflater.from(context), this, true)
            onViewInit(context, types)
            initAttrs(context, attrs)
        } finally {
            types.recycle()
        }
    }

    /**
     * Text
     */
    val TypedArray.text: String?
        get() = getString(R.styleable.CustomView_android_text)

    val TypedArray.title: String?
        get() = getString(R.styleable.CustomView_android_title)

    val TypedArray.hint: String?
        get() = getString(R.styleable.CustomView_android_hint)

    val TypedArray.inputHint: String?
        get() = getString(R.styleable.CustomView_input_hint) ?: ""

    /**
     * Input type
     */
    val TypedArray.maxLength: Int
        get() = getInt(R.styleable.CustomView_android_maxLength, 256)

    val TypedArray.maxLines: Int
        get() = getInt(R.styleable.CustomView_android_maxLines, 1)

    val TypedArray.textAllCaps: Boolean
        get() = getBoolean(R.styleable.CustomView_android_textAllCaps, false)

    /**
     * Color
     */
    val TypedArray.tint: Int
        get() {
            return getColor(R.styleable.CustomView_android_tint, Color.BLACK)
        }

    val TypedArray.backgroundTint: Int
        get() {
            return getColor(R.styleable.CustomView_android_backgroundTint, Color.WHITE)
        }

    val TypedArray.textColor: Int
        get() {
            return getColor(R.styleable.CustomView_android_textColor, Color.BLACK)
        }

    val TypedArray.hintColor: Int
        get() {
            return getColor(R.styleable.CustomView_android_textColorHint, Color.DKGRAY)
        }

    /**
     * Drawable
     */
    val TypedArray.drawableStart: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_drawableStart)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawableEnd: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_drawableEnd)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.drawable: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_drawable)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.src: Drawable?
        get() {
            return getDrawable(R.styleable.CustomView_android_src)
                ?.constantState?.newDrawable()?.mutate()
        }

    val TypedArray.srcRes: Int
        get() {
            return getResourceId(R.styleable.CustomView_android_src, 0)
        }

    val TypedArray.background: Int
        get() {
            return getResourceId(R.styleable.CustomView_android_background, 0)
        }

    /**
     * Checkable
     */
    val TypedArray.checkable: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checkable, false)

    val TypedArray.checked: Boolean
        get() = getBoolean(R.styleable.CustomView_android_checked, false)

    /**
     * Padding
     */
    val TypedArray.paddingStart: Int
        get() = getDimensionPixelSize(R.styleable.CustomView_android_paddingStart, 0)

    val TypedArray.paddingEnd: Int
        get() = getDimensionPixelSize(R.styleable.CustomView_android_paddingEnd, 0)

    val TypedArray.paddingTop: Int
        get() = getDimensionPixelSize(R.styleable.CustomView_android_paddingTop, 0)

    val TypedArray.paddingBottom: Int
        get() = getDimensionPixelSize(R.styleable.CustomView_android_paddingBottom, 0)

    fun View.backgroundTintRes(@ColorRes colorRes: Int) {
        backgroundTint(color(colorRes))
    }

    fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(app, res)
    }

    fun View.backgroundTint(@ColorInt color: Int) {
        post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                background?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                @Suppress("DEPRECATION")
                background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    /**
     * config icon header
     */
    val TypedArray.back: Boolean
        get() = getBoolean(R.styleable.CustomView_back, true)

    val TypedArray.cancel: Boolean
        get() = getBoolean(R.styleable.CustomView_cancel, true)

    val TypedArray.hideAction: Boolean
        get() = getBoolean(R.styleable.CustomView_hideAction, true)

}