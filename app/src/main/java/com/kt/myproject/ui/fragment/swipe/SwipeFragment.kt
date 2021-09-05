package com.kt.myproject.ui.fragment.swipe

import com.kt.myproject.base.BaseDialog
import com.kt.myproject.base.viewModel
import com.kt.myproject.databinding.FragmentSwipeBinding
import com.kt.myproject.repository.data.CardItem

class SwipeFragment : BaseDialog<FragmentSwipeBinding>(FragmentSwipeBinding::inflate) {

    private val vm by lazy { viewModel(SwipeVM::class) }

    private val adapter = SwipeAdapter()

    override fun onViewCreated() {
        vm.fetchBanks()

    }

    override fun onLiveDataObserve() {
        vm.banksEvent.observe {
            configAdapter(it)
        }
    }

    private fun configAdapter(list: List<CardItem>) {
        adapter.set(list)
        adapter.bind(binding.recyclerView)
    }

}