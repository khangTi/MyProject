package com.kt.myproject.ui.fragment.repeat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.RepeatBinding
import com.kt.myproject.ui.adapter.RepeatAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RepeatFragment : BaseFragment<RepeatBinding>(RepeatBinding::inflate) {

    private var job: Job? = null

    private val adapter = RepeatAdapter()

    private val viewModel: RepeatVM by viewModels()

    override fun onViewCreated() {
        MainScope().launch { job = viewModel.repeatLog() }
        setupAdapter()
    }

    override fun onLiveDataObserve() {
        viewModel.listLiveData.obsever {
            adapter.set(it)
            adapter.notifyDataSetChanged()
            binding.repeatRecycler.smoothScrollToPosition(it.size)
        }
    }

    private fun setupAdapter() {
        adapter.set(viewModel.listLiveData.value)
        adapter.bind(binding.repeatRecycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

}