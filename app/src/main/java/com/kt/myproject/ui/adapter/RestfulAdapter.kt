package com.kt.myproject.ui.adapter

import androidx.viewbinding.ViewBinding
import com.kt.myproject.base.BaseBindRecyclerView
import com.kt.myproject.databinding.ItemRepeatBinding
import com.kt.myproject.ex.ItemInflating
import com.kt.myproject.ex.load
import com.kt.myproject.repository.model.User

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestfulAdapter : BaseBindRecyclerView<User>() {

    override fun itemInflating(item: User, position: Int): ItemInflating {
        return ItemRepeatBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: User, position: Int) {
        if (this is ItemRepeatBinding) {
            itemRepeatTitle.text = item.name
            itemRepeatContent.text = item.email
        }
    }

}