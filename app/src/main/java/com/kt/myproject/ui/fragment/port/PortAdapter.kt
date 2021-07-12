package com.kt.myproject.ui.fragment.port

import androidx.viewbinding.ViewBinding
import com.kt.myproject.base.BaseBindRecyclerView
import com.kt.myproject.base.ItemInflating
import com.kt.myproject.databinding.ItemHomeBinding

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class PortAdapter : BaseBindRecyclerView<String>() {

    override fun itemInflating(item: String, position: Int): ItemInflating {
        return ItemHomeBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: String, position: Int) {
        if (this !is ItemHomeBinding) return
        itemHomeLabel.text = item
    }


}