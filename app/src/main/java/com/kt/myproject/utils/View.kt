package com.kt.myproject.utils

import android.view.View

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

fun View.show() {
    if (getMainLooper) {
        this.visibility = View.VISIBLE
    } else {
        post {
            this.visibility = View.VISIBLE
        }
    }
}

fun View.hide() {
    if (getMainLooper) {
        this.visibility = View.INVISIBLE
    } else {
        post {
            this.visibility = View.INVISIBLE
        }
    }
}

fun View.gone() {
    if (getMainLooper) {
        this.visibility = View.GONE
    } else {
        post {
            this.visibility = View.GONE
        }
    }
}