package com.kt.myproject.ui.fragment.member

import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.MemberBinding


class MemberFragment : BaseFragment<MemberBinding>(MemberBinding::inflate) {

    override fun onViewCreated() {
        binding.loadingView.startTimeOut(60)

        binding.button.setOnClickListener {
            binding.loadingView.startTimeOut(60)
        }
    }

    override fun onLiveDataObserve() {
    }

}