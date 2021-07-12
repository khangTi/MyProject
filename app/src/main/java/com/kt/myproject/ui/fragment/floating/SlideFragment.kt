package com.kt.myproject.ui.fragment.floating

import com.kt.myproject.R
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.SlideBinding

class SlideFragment : BaseFragment<SlideBinding>(SlideBinding::inflate) {

    override fun onViewCreated() {
        binding.floatingSlider.initSlider(
            listOf(
                R.mipmap.img_adv1,
                R.mipmap.img_adv2,
                R.mipmap.img_adv3
            )
        )
    }

    override fun onLiveDataObserve() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.floatingSlider.destroySlide()
    }

}