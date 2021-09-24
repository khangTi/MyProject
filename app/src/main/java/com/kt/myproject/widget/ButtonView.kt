package com.kt.myproject.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.ButtonViewBinding
import com.kt.myproject.ex.animClick

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/15
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ButtonView(context: Context, attributeSet: AttributeSet) :
    AppBindCustomView<ButtonViewBinding>(context, attributeSet, ButtonViewBinding::inflate) {

    override fun onViewInit(context: Context, types: TypedArray) {
        val text = types.text
        binding.buttonAction.text = text
    }

    fun actionClickListener(block: () -> Unit) {
        binding.buttonAction.animClick {
            block()
        }
    }

}