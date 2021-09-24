package com.kt.myproject.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.HeaderViewBinding
import com.kt.myproject.ex.hide
import com.kt.myproject.ex.show

class HeaderView(context: Context, attributeSet: AttributeSet) :
    AppBindCustomView<HeaderViewBinding>(context, attributeSet, HeaderViewBinding::inflate) {

    override fun onViewInit(context: Context, types: TypedArray) {
        binding.headerTitle.text = types.title

        if (types.hideAction) {
            binding.headerAction.hide()
            binding.headerTitle.gravity = Gravity.LEFT
        } else {
            binding.headerAction.show()
            binding.headerTitle.gravity = Gravity.CENTER
        }
    }

}