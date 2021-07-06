package com.kt.myproject.ui.fragment.floating

import com.kt.myproject.R
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.FloatingActionBinding

class FloatingFragment : BaseFragment<FloatingActionBinding>(FloatingActionBinding::inflate) {

    override fun onViewCreated() {
        binding.floatingSlider.initSlider(listOf(R.mipmap.img_adv_1, R.mipmap.img_adv_2))
    }

    override fun onLiveDataObserve() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.floatingSlider.destroySlide()
    }

}