package com.kt.myproject.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun ImageView.load(url: String) {
    Glide.with(context).load(url).into(this)
}