package com.kt.myproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kt.myproject.R
import com.kt.myproject.databinding.HeaderViewBinding
import com.kt.myproject.utils.hide
import com.kt.myproject.utils.show

class HeaderView(context: Context, attributeSet: AttributeSet) :
        ConstraintLayout(context, attributeSet) {

    private val binding = HeaderViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val type = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomView, 0, 0)

        val title = type.getString(R.styleable.CustomView_android_text)
        binding.headerTitle.text = title

        val status = type.getBoolean(R.styleable.CustomView_hideAction, false)
        if (status){
            binding.headerAction.hide()
            binding.headerTitle.gravity = Gravity.LEFT
        } else{
            binding.headerAction.show()
            binding.headerTitle.gravity = Gravity.CENTER
        }
    }

}