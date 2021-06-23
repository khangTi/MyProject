package com.kt.myproject.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.kt.myproject.ui.activity.MainActivity

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(val inflate: Inflate<VB>) : Fragment(), BaseView {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    /**
     * [BaseFragment] required implements
     */
    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    /**
     * [BaseView] implement
     */
    final override val nav get() = findNavController()

    /**
     * [BaseFragment] properties
     */

    open fun activity(): MainActivity {
        if (activity !is MainActivity) throw ClassCastException("BaseFragment must be owned in BaseActivity")
        return activity as MainActivity
    }

    fun startClear(cls: Class<*>) {
        activity().run {
            val intent = Intent(this, cls)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(intent)
            this.finish()
        }
    }

    fun <T> MutableLiveData<T>.obsever(block: (T) -> Unit) {
        this.observe(viewLifecycleOwner, {
            block(it)
        })
    }

}