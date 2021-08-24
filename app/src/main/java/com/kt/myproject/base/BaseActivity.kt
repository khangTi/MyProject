package com.kt.myproject.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.kt.myproject.utils.Logger

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {

    val log by lazy {
        Logger(this::class.simpleName.toString())
    }

    lateinit var binding: VB

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
    abstract fun viewBinding(): VB

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

    fun <T> MutableLiveData<T>.obsever(block: (T) -> Unit) {
        this.observe(this@BaseActivity, {
            it?.let { it1 -> block(it1) }
        })
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer(block))
    }

    fun <T> NonNullLiveData<T?>.observe(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer {
            it ?: return@Observer
            block(it)
        })
    }

}