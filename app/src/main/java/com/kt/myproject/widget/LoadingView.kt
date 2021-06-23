package com.kt.myproject.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowInsets
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kt.myproject.R
import com.kt.myproject.app.app
import kotlinx.android.synthetic.main.loading.view.*
import kotlin.math.round

class LoadingView : ConstraintLayout {

    private var timer: CountDownTimer? = null

    private var withLoading = 0

    private var valuePlus = 0

    private val activity = context as Activity

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        LayoutInflater.from(context).inflate(R.layout.loading, this,true)
        configUI()
    }

    private fun configUI() {
        loadingImageBg.layoutParams.width = activity.width()
        loadingImageParent.layoutParams.width = activity.width()
        loadingImageColor.layoutParams.width = activity.width()
    }

    @SuppressLint("ResourceAsColor")
    fun startTimeOut(s: Int) {
        resetView()
        val time = s * 1000L
        valuePlus = (activity.width() / (s))
        timer = object : CountDownTimer(time, 1000) {

            override fun onTick(mill: Long) {
                if (withLoading >= activity.width()) {
                    timer?.cancel()
                    return
                }
                when {
                    (mill / 1000) <= (s / 3) -> loadingImageColor.tint(R.color.red)
                    (mill / 1000) <= (s / 2) -> loadingImageColor.tint(R.color.yellow)
                }
                withLoading += valuePlus
                if(withLoading == valuePlus * 2) withLoading + valuePlus
                loadingImageParent.setPadding(0, 0, withLoading, 0)
            }

            override fun onFinish() {
                loadingImageParent.setPadding(0, 0, activity.width(), 0)
                loadingImageColor.tint(R.color.gray)
            }
        }.start()
    }

    fun stopTimeOut() {
        timer?.cancel()
    }

    private fun resetView(){
        timer?.cancel()
        timer = null
        withLoading = 0
        loadingImageParent.setPadding(0, 0, 0, 0)
        loadingImageColor.tint(R.color.teal_200)
    }

    private fun Activity.width(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = this.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            (windowMetrics.bounds.width() - insets.left - insets.right) - 150
        } else {
            val displayMetrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
            (displayMetrics.widthPixels) - 150
        }
    }

    private fun ImageView.tint(@ColorInt color: Int) {
        this.post { this.setColorFilter(color(color)) }
    }

    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(app, res)
    }

}