package com.kt.myproject.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.kt.myproject.app.app

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

var handler = Handler(Looper.getMainLooper())

fun post(block: () -> Unit) {
    handler.post { block() }
}

fun post(duration: Long, block: () -> Unit) {
    handler.postDelayed({ block() }, duration)
}

val getMainLooper = Looper.getMainLooper() == Looper.myLooper()

fun toast(mess: String) {
    if (getMainLooper) {
        Toast.makeText(app, mess, Toast.LENGTH_SHORT).show()
    } else {
        post { Toast.makeText(app, mess, Toast.LENGTH_SHORT).show() }
    }
}