package com.kt.myproject.ui.activity

import com.kt.myproject.base.BaseActivity
import com.kt.myproject.base.activityVM
import com.kt.myproject.databinding.ActivityCallBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.ui.fragment.call.CallVM

class CallActivity : BaseActivity<ActivityCallBinding>() {

    private val vm by lazy { activityVM(CallVM::class) }

    override fun viewBinding(): ActivityCallBinding {
        return ActivityCallBinding.inflate(layoutInflater)
    }

    override fun onViewCreated() {}

    override fun onLiveDataObserve() {
        vm.idCallSingle.observe { toast(it) }
    }

}