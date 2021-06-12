package com.kt.myproject.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.kt.myproject.app.app
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

fun String.readAsset(): String {
    val sb = StringBuilder()
    BufferedReader(InputStreamReader(app.assets.open(this))).useLines { lines ->
        lines.forEach {
            sb.append(it)
        }
    }
    return sb.toString()
}

fun String.toArrayObject(): JsonArray {
    return Gson().fromJson(this, JsonArray::class.java)
}

fun <T> String.toArray(cls: Class<T>): ArrayList<T> {
    val arrayObject = this.toArrayObject()
    val list = arrayListOf<T>()
    arrayObject.forEach {
        val obj = Gson().fromJson(it, cls)
        list.add(obj)
    }
    return list
}