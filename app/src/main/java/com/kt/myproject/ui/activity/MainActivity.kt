package com.kt.myproject.ui.activity

import com.kt.myproject.base.BaseActivity
import com.kt.myproject.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun viewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onViewCreated() {

    }

    override fun onLiveDataObserve() {

    }


}