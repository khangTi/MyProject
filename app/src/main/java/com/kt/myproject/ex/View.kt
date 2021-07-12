package com.kt.myproject.ex

import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

fun View.show() {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        this.visibility = View.VISIBLE
    } else {
        post {
            this.visibility = View.VISIBLE
        }
    }
}

fun View.hide() {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        this.visibility = View.INVISIBLE
    } else {
        post {
            this.visibility = View.INVISIBLE
        }
    }
}

fun View.gone() {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        this.visibility = View.GONE
    } else {
        post {
            this.visibility = View.GONE
        }
    }
}

fun View.animClick(block: () -> Unit) {
    this.setOnClickListener {
        val anim = AlphaAnimation(0f, 1f)
        anim.duration = 250
        anim.interpolator = AccelerateDecelerateInterpolator()
        this.startAnimation(anim)
        this.hide()
        anim.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                this@animClick.show()
                block()
            }

            override fun onAnimationRepeat(animation: Animation?) {}

        })
    }
}