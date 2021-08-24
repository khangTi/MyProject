package com.kt.myproject.ui.activity

import com.kt.myproject.base.BaseActivity
import com.kt.myproject.databinding.ActivityCallBinding

class CallActivity : BaseActivity<ActivityCallBinding>() {

    override fun viewBinding(): ActivityCallBinding {
        return ActivityCallBinding.inflate(layoutInflater)
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {
    }


}