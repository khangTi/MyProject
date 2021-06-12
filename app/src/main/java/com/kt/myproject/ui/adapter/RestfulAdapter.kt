package com.kt.myproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kt.myproject.base.BaseRecyclerAdapterBinding
import com.kt.myproject.databinding.ItemRepeatBinding
import com.kt.myproject.repository.model.User
import com.kt.myproject.utils.load

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestfulAdapter : BaseRecyclerAdapterBinding<User, ItemRepeatBinding>() {

    override fun getBinding(context: Context, parent: ViewGroup): ItemRepeatBinding {
        return ItemRepeatBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun View.onBindModel(model: User, position: Int, view: ItemRepeatBinding) {
        view.itemRepeatImage.load(model.avatar)
        view.itemRepeatTitle.text = model.name
        view.itemRepeatContent.text = model.email
    }
}