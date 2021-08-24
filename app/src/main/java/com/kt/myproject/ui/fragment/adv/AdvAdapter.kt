package com.kt.myproject.ui.fragment.adv

import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.kt.myproject.base.BaseListAdapter
import com.kt.myproject.base.EventLiveData
import com.kt.myproject.databinding.AdvImageItemBinding
import com.kt.myproject.databinding.AdvVideoItemBinding
import com.kt.myproject.ex.ItemInflating
import com.kt.myproject.ex.load

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/08/24
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AdvAdapter : BaseListAdapter<AdvItem>() {

    /**
     * Current of viewpager position adapt this adapter
     */
    var currentPosition: Int = -1

    var onPageChanged = EventLiveData<Boolean>()

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun get(position: Int): AdvItem? {
        if (currentList.isEmpty()) return null
        val realPosition = position % currentList.size
        if (realPosition !in 0..lastIndex) return null
        return currentList[realPosition]
    }

    override fun itemInflating(item: AdvItem, position: Int): ItemInflating {
        return when {
            item.imageRes != null -> AdvImageItemBinding::inflate
            else -> AdvVideoItemBinding::inflate
        }
    }

    override fun ViewBinding.onBindItem(item: AdvItem, position: Int) {
        currentPosition = position
        when (this) {
            is AdvImageItemBinding -> {
                advImageView.load(item.imageRes!!)
            }
            is AdvVideoItemBinding -> {
                item.videoController?.also {
                    it.stopVideo()
                }
                item.videoController = VideoController(playerView, item.videoRes!!).also {
                    it.onVideoStopped = {
                        viewPager?.also { viewPager ->
                            when {
                                currentList.size == 1 -> {
                                    it.playVideo()
                                }
                                position == currentList.size - 1 -> {
                                    viewPager.setCurrentItem(0, true)
                                }
                                else -> {
                                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                                }
                            }
                        }
                    }
                    it.playVideo()
                }
            }
        }
    }

    /**
     * ViewHolder util
     */
    private var viewPager: ViewPager2? = null

    fun bindToViewPager(viewPager: ViewPager2) {
        this.viewPager = viewPager
        viewPager.adapter = this
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_DRAGGING -> {
                    }
                    ViewPager.SCROLL_STATE_IDLE -> {
                        onPageChanged.postValue(true)
                        if (currentPosition != -1) {
                            get(currentPosition)?.videoController?.stopVideo()
                        }
                        currentPosition = viewPager.currentItem
                        get(currentPosition)?.videoController?.playVideo()
                    }
                }
            }
        })
    }

}