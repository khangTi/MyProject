package com.kt.myproject.ui.fragment.adv

import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.AdvBinding

class AdvFragment : BaseFragment<AdvBinding>(AdvBinding::inflate) {

    private val viewModel: AdvVM by viewModels()

    private val advAdapter = AdvAdapter()

    override fun onViewCreated() {
        viewModel.fetchAdvList()
        viewModel.countdownToNextSlide()
        binding.advViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewModel.countdownToNextSlide()
            }
        })
    }

    override fun onLiveDataObserve() {
        advAdapter.onPageChanged.observe { handlerViewPagerChange() }
        viewModel.imagesLiveData.observe { configAdapter(it) }
        viewModel.pageLiveData.observe { handlerViewPager() }
    }

    private fun configAdapter(list: List<AdvItem>) {
        advAdapter.set(list)
        advAdapter.bindToViewPager(binding.advViewPager)
    }

    private fun handlerViewPager() {
        var i = binding.advViewPager.currentItem
        if (i == advAdapter.currentList.size - 1) {
            binding.advViewPager.setCurrentItem(0, true)
        } else {
            binding.advViewPager.setCurrentItem(++i, true)
        }
    }

    private fun handlerViewPagerChange() {
        val i = binding.advViewPager.currentItem
        if (advAdapter.get(i)?.isImage == true) {
            viewModel.countdownToNextSlide()
        }
    }

}