package com.kt.myproject.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>: Fragment(), BaseView {

    lateinit var view : VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = getBinding(inflater, container)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    /**
     * [BaseFragment] required implements
     */
    abstract fun getBinding(inflater : LayoutInflater, viewGroup : ViewGroup?) : VB

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    /**
     * [BaseView] implement
     */
    final override val nav get() = findNavController()

    /**
     * [BaseFragment] properties
     */

    open fun activity(): BaseActivity {
        if (activity !is BaseActivity) throw ClassCastException("BaseFragment must be owned in BaseActivity")
        return activity as BaseActivity
    }

    fun startClear(cls: Class<*>) {
        activity().run {
            val intent = Intent(this, cls)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
            this.finish()
        }

    }

}