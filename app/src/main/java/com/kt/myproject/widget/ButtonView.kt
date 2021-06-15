package com.kt.myproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kt.myproject.R
import com.kt.myproject.databinding.ButtonViewBinding
import com.kt.myproject.utils.animClick

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/15
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ButtonView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding = ButtonViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val types = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomView, 0, 0)

        val text = types.getString(R.styleable.CustomView_android_text)
        binding.buttonAction.text = text
    }

    fun actionClickListener(block: () -> Unit) {
        binding.buttonAction.animClick {
            block()
        }
    }

}