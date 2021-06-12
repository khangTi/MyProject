package com.kt.myproject.ui.fragment.restful

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.RestfulBinding
import com.kt.myproject.ui.adapter.RestfulAdapter
import com.kt.myproject.utils.toast

class RestfulFragment : BaseFragment<RestfulBinding>() {

    private val adapter = RestfulAdapter()

    private val viewModel: RestfulVM by viewModels()

    override fun getBinding(inflater: LayoutInflater, vg: ViewGroup?): RestfulBinding {
        return RestfulBinding.inflate(inflater, vg, false)
    }

    override fun onViewCreated() {
        viewModel.getUser()
        adapter.bind(view.restfulRecycler)
    }

    override fun onLiveDataObserve() {
        viewModel.userLiveData.obsever {
            adapter.set(it.listUser)
            adapter.notifyDataSetChanged()
        }
        viewModel.userError.obsever {
            toast("get user fail : $it")
        }
    }

}