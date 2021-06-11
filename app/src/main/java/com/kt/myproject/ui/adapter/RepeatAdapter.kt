package com.kt.myproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kt.myproject.base.BaseRecyclerAdapterBinding
import com.kt.myproject.databinding.ItemRepeatBinding
import com.kt.myproject.repository.model.RepeatData
import com.kt.myproject.utils.load

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RepeatAdapter : BaseRecyclerAdapterBinding<RepeatData, ItemRepeatBinding>() {

    override fun getBinding(context: Context, parent: ViewGroup): ItemRepeatBinding {
        return ItemRepeatBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun View.onBindModel(model: RepeatData, position: Int, view: ItemRepeatBinding) {
        view.itemRepeatImage.load(model.image)
        view.itemRepeatTitle.text = model.actor
        view.itemRepeatContent.text = model.date
    }

}