package com.kt.myproject.ui.fragment.call

import com.kt.myproject.base.BaseFragment
import com.kt.myproject.base.NavigationId
import com.kt.myproject.base.activityVM
import com.kt.myproject.databinding.FragmentConnectBinding

class ConnectFragment : BaseFragment<FragmentConnectBinding>(FragmentConnectBinding::inflate) {

    private val vm by lazy { activityVM(CallVM::class) }

    override fun onViewCreated() {
        binding.connectAction.actionClickListener { checkPermissionAndRequest() }
    }

    override fun onLiveDataObserve() {}

    override fun onPermissionGranted(granted: String) {
        vm.idCallSingle.postValue(binding.connectAppInput.text.toString())
        navigate(NavigationId.call)
    }

}