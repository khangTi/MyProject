package com.kt.myproject.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {

    lateinit var binding : VB

    /**
     * [AppCompatActivity] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBinding()
        setContentView(binding.root)
        onViewCreated()
        onLiveDataObserve()
    }

    /**
     * [BaseActivity] abstract implements
     */
    abstract fun viewBinding() : VB

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    /**
     * [BaseView] implement
     */
    final override val nav get() = findNavController(navigationHostId())


    /**
     * [BaseActivity] properties
     */
    open fun navigationHostId(): Int {
        return 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}