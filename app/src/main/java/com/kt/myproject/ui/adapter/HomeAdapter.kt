package com.kt.myproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kt.myproject.base.BaseRecyclerAdapter
import com.kt.myproject.databinding.ItemHomeBinding

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */class HomeAdapter : BaseRecyclerAdapter<String, ItemHomeBinding>() {

    override fun getBinding(context: Context, parent: ViewGroup): ItemHomeBinding {
        return ItemHomeBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun View.onBindModel(model: String, position: Int, view: ItemHomeBinding) {
        view.itemHomeLabel.text = model
    }
}