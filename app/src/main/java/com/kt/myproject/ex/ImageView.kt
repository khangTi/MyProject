package com.kt.myproject.ex

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kt.myproject.app.app
import java.io.File

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

fun ImageView.load(file: File) {
    Glide.with(app).load(file).into(this).clearOnDetach()
}

fun ImageView.load(uri: Uri) {
    Glide.with(app).load(uri).into(this).clearOnDetach()
}