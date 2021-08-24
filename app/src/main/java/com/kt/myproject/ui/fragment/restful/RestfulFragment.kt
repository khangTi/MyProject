package com.kt.myproject.ui.fragment.restful

import androidx.fragment.app.viewModels
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.RestfulBinding
import com.kt.myproject.ex.toast
import com.kt.myproject.ui.adapter.RestfulAdapter

class RestfulFragment : BaseFragment<RestfulBinding>(RestfulBinding::inflate) {

    private val adapter = RestfulAdapter()

    private val viewModel: RestfulVM by viewModels()

    override fun onViewCreated() {
        viewModel.getUser()
        adapter.bind(binding.restfulRecycler)
    }

    override fun onLiveDataObserve() {
        viewModel.userLiveData.obsever {
            adapter.set(it)
            adapter.notifyDataSetChanged()
        }
        viewModel.userError.obsever {
            toast("get user fail : $it")
        }
    }

}