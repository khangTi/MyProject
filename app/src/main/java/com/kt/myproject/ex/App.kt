package com.kt.myproject.ex

import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.kt.myproject.app.app
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

private val resources: Resources get() = app.resources

val screenWidth: Int
    get() {
        return Resources.getSystem().displayMetrics.widthPixels
    }

val screenHeight: Int
    get() {
        return Resources.getSystem().displayMetrics.heightPixels
    }

val screenRatio: Float
    get() {
        return screenHeight.toFloat() / screenWidth
    }

val packageName: String get() = app.applicationContext.packageName

fun open(file: File) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        app.startActivity(Intent.createChooser(intent, ""))
    } catch (e: Exception) {
    }
}

val statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = app.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) result = app.resources.getDimensionPixelSize(resourceId)
        return result
    }

val navigationBarHeight: Int
    get() {
        val resources: Resources = app.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

fun restartApp() {
    val intent = app.packageManager.getLaunchIntentForPackage(packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    app.startActivity(intent)
}

fun color(color: String): Int {
    return try {
        Color.parseColor(color)
    } catch (e: Exception) {
        Log.e("color", "parse color Exception $e")
        Color.BLUE
    }
}

fun int(@IntegerRes res: Int): Int {
    return app.resources.getInteger(res)
}

@RequiresApi(Build.VERSION_CODES.N)
fun stringHtml(@StringRes res: Int): String {
    return Html.fromHtml(string(res), Html.FROM_HTML_MODE_LEGACY).toString()
}

fun uri(resId: Int): Uri {
    return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(resId))
            .appendPath(resources.getResourceTypeName(resId))
            .appendPath(resources.getResourceEntryName(resId))
            .build()
}

fun readAsset(filename: String): String {
    val sb = StringBuilder()
    BufferedReader(InputStreamReader(app.assets.open(filename))).useLines { lines ->
        lines.forEach {
            sb.append(it)
        }
    }
    return sb.toString()
}

fun getResName(v: View?): String {
    v ?: return ""
    return try {
        app.resources.getResourceEntryName(v.id)
    } catch (e: Resources.NotFoundException) {
        ""
    }
}

fun getResName(id: Int): String {
    return try {
        app.resources.getResourceEntryName(id)
    } catch (e: Resources.NotFoundException) {
        ""
    }
}