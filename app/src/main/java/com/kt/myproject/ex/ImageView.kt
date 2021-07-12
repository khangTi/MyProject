package com.kt.myproject.ex

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kt.myproject.app.app

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun ImageView.load(url: String) {
    Glide.with(app).load(url).into(this).clearOnDetach()
}

fun ImageView.load(image: Int) {
    Glide.with(app).load(image).into(this).clearOnDetach()
}