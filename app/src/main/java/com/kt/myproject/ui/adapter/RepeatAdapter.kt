package com.kt.myproject.ui.adapter

import androidx.viewbinding.ViewBinding
import com.kt.myproject.base.BaseListAdapter
import com.kt.myproject.databinding.ItemRepeatBinding
import com.kt.myproject.ex.ItemInflating
import com.kt.myproject.ex.load
import com.kt.myproject.repository.model.RepeatData

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RepeatAdapter : BaseListAdapter<RepeatData>(RepeatData.itemDiffer) {

    override fun itemInflating(item: RepeatData, position: Int): ItemInflating {
        return ItemRepeatBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: RepeatData, position: Int) {
        if (this is ItemRepeatBinding) {
            itemRepeatImage.load(item.image)
            itemRepeatTitle.text = item.actor
            itemRepeatContent.text = item.date
        }
    }

}