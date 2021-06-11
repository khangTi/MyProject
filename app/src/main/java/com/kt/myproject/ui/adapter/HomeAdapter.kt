package com.kt.myproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kt.myproject.base.BaseRecyclerAdapterBinding
import com.kt.myproject.databinding.ItemHomeBinding
import com.kt.myproject.repository.UI

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */class HomeAdapter : BaseRecyclerAdapterBinding<UI, ItemHomeBinding>() {

    override fun getBinding(context: Context, parent: ViewGroup): ItemHomeBinding {
        return ItemHomeBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun View.onBindModel(model: UI, position: Int, view: ItemHomeBinding) {
        view.itemHomeLabel.text = model.toString()
    }
}