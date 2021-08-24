package com.kt.myproject.ui.adapter

import androidx.viewbinding.ViewBinding
import com.kt.myproject.base.BaseBindRecyclerView
import com.kt.myproject.databinding.ItemHomeBinding
import com.kt.myproject.ex.ItemInflating
import com.kt.myproject.repository.data.UI

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class HomeAdapter : BaseBindRecyclerView<UI>() {

    override fun itemInflating(item: UI, position: Int): ItemInflating {
        return ItemHomeBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: UI, position: Int) {
        if (this is ItemHomeBinding) {
            itemHomeLabel.text = item.toString()
        }
    }

}