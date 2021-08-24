package com.kt.myproject.ui.fragment.repeat

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.RepeatBinding
import com.kt.myproject.ui.adapter.RepeatAdapter
import kotlinx.coroutines.launch

class RepeatFragment : BaseFragment<RepeatBinding>(RepeatBinding::inflate) {

    private val adapter = RepeatAdapter()

    private val viewModel: RepeatVM by viewModels()

    override fun onViewCreated() {
        lifecycleScope.launch {
            viewModel.repeatLog()
        }
        setupAdapter()
    }

    override fun onLiveDataObserve() {
        viewModel.listLiveData.obsever {
            adapter.set(it)
            binding.repeatRecycler.smoothScrollToPosition(it.size)
        }
    }

    private fun setupAdapter() {
        adapter.set(viewModel.listLiveData.value)
        adapter.bind(binding.repeatRecycler)
    }

}