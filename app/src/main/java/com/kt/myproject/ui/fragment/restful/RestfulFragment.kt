package com.kt.myproject.ui.fragment.restful

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.RestfulBinding

class RestfulFragment : BaseFragment<RestfulBinding>() {

    override fun getBinding(inflater: LayoutInflater, vg: ViewGroup?): RestfulBinding {
        return RestfulBinding.inflate(inflater, vg, false)
    }

    override fun onViewCreated() {

    }

    override fun onLiveDataObserve() {

    }

}