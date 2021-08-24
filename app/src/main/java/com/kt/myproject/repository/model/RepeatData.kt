package com.kt.myproject.repository.model

import androidx.recyclerview.widget.DiffUtil

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RepeatData {

    var actor: String = ""
    var image: String = ""
    var date: String = ""

    constructor(actor: String, image: String, date: String) {
        this.actor = actor
        this.image = image
        this.date = date
    }

    companion object{
        val itemDiffer
            get() = object : DiffUtil.ItemCallback<RepeatData>() {
                override fun areItemsTheSame(oldItem: RepeatData, newItem: RepeatData): Boolean {
                    return oldItem.actor === newItem.actor
                }

                override fun areContentsTheSame(oldItem: RepeatData, newItem: RepeatData): Boolean {
                    return oldItem.image == newItem.image
                }

            }
    }

}