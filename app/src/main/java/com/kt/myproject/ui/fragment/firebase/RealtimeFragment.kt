package com.kt.myproject.ui.fragment.firebase

import androidx.lifecycle.lifecycleScope
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.base.viewModel
import com.kt.myproject.databinding.FragmentRealtimeBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.repository.model.RealtimeData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RealtimeFragment : BaseFragment<FragmentRealtimeBinding>(FragmentRealtimeBinding::inflate) {

    private val vm by lazy { viewModel(RealtimeVM::class) }

    override fun onViewCreated() {
        vm.eventRealtime()
    }

    override fun onLiveDataObserve() {
        vm.realtimeDataEvent.observe { handleDataSuccess(it) }
        vm.realtimeFailedEvent.observe { handleDataFail(it) }
    }

    private fun handleDataSuccess(it: RealtimeData) {
        binding.realtimeEmail.text = it.email
        binding.realtimePassword.text = it.password
    }

    private fun handleDataFail(s: String) {
        toast(s)
        lifecycleScope.launch {
            delay(1000)
            vm.eventRealtime()
        }
    }

}