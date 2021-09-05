package com.kt.myproject.ui.fragment.swipe

import androidx.viewbinding.ViewBinding
import com.kt.myproject.base.BaseListAdapter
import com.kt.myproject.databinding.FragmentSwipeItemBinding
import com.kt.myproject.ex.ItemInflating
import com.kt.myproject.ex.isGone
import com.kt.myproject.repository.data.CardItem
import com.kt.myproject.utils.SwipeLayout

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/01
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SwipeAdapter : BaseListAdapter<CardItem>() {

    private var layoutItemSwipeLast: FragmentSwipeItemBinding? = null

    private var positionSwipeLast: Int = -1

    override fun itemInflating(item: CardItem, position: Int): ItemInflating {
        return FragmentSwipeItemBinding::inflate
    }

    override fun ViewBinding.onBindItem(item: CardItem, position: Int) {
        if (this is FragmentSwipeItemBinding) {
            itemSwipeTitle.text = item.shortName
            itemSwipeContent.text = item.bankName
            itemSwipeType.text = item.shortName[0].toString()
            if (position > 0) {
                val posSection = position - 1
                val matchingName =
                    item.shortName[0] == currentList[posSection].shortName[0]
                itemSwipeType.isGone(matchingName)
            } else {
                itemSwipeType.isGone(false)
            }
            /**
             * handle swipe
             */
            if (position != positionSwipeLast) itemSwipeLayout.close(false)
            itemSwipeLayout.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
                override fun onOpen(direction: Int, isContinuous: Boolean) {
                    if (positionSwipeLast != position) {
                        layoutItemSwipeLast?.itemSwipeLayout?.close()
                    }
                    layoutItemSwipeLast = this@onBindItem
                    positionSwipeLast = position
                }

                override fun onClose() {}

            })
        }
    }
}