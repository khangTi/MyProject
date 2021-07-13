package com.kt.myproject.ui.fragment.port

import androidx.viewbinding.ViewBinding
import com.kt.myproject.R
import com.kt.myproject.app.app
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
class PortAdapter : BaseBindRecyclerView<PortDataItem>() {

    override fun itemInflating(item: PortDataItem, position: Int): ItemInflating {
        return ItemHomeBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: PortDataItem, position: Int) {
        if (this !is ItemHomeBinding) return
        itemHomeLabel.text = "${item.ip} - ${item.name}"
        if (item.isColor) itemHomeLabel.setTextColor(app.getColor(R.color.red)) else itemHomeLabel.setTextColor(
            app.getColor(R.color.black)
        )
    }


}

data class PortDataItem(var ip: String = "", var name: String = "", var isColor: Boolean = false)