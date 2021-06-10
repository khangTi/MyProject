package com.kt.myproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.kt.myproject.R

abstract class BaseDialog<VB : ViewBinding> : DialogFragment(), BaseView {

    lateinit var view: VB

    /**
     * [DialogFragment] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, style())
        dialog?.window?.attributes?.windowAnimations = animStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.window?.attributes?.windowAnimations = animEnd()
    }

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

    override fun onStart() {
        super.onStart()
        when (style()) {
            R.style.App_Dialog_FullScreen,
            R.style.App_Dialog_FullScreen_Transparent -> dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    /**
     * [BaseDialog] Required implements
     */
    abstract fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): VB

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    protected open fun style(): Int {
        return R.style.App_Dialog_FullScreen_Transparent
    }

    open fun animStart(): Int {
        return R.style.App_Dialog_FullScreen
    }

    open fun animEnd(): Int {
        return R.style.App_Dialog_FullScreen
    }

    /**
     * [BaseView] implement
     */
    final override val nav get() = findNavController()

}