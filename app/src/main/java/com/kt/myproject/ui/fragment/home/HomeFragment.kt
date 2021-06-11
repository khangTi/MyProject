package com.kt.myproject.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.base.NavigationId
import com.kt.myproject.databinding.HomeBinding
import com.kt.myproject.repository.UI
import com.kt.myproject.repository.list
import com.kt.myproject.ui.adapter.HomeAdapter

class HomeFragment : BaseFragment<HomeBinding>() {

    private val adapter = HomeAdapter()

    override fun getBinding(inflater: LayoutInflater, vg: ViewGroup?): HomeBinding {
        return HomeBinding.inflate(inflater, vg, false)
    }

    override fun onViewCreated() {
        setupAdapter()
    }

    override fun onLiveDataObserve() {}

    private fun setupAdapter() {
        adapter.set(list)
        adapter.bind(view.homeRecyclerView)
        adapter.onItemClick { s, _ -> navigationUI(s) }
    }

    private fun navigationUI(s: UI) {
        when (s) {
            UI.BASIC_COROUTINE -> navigate(NavigationId.repeat)
            UI.RETROFIT_COROUTINE -> navigate(NavigationId.restful)
        }
    }

}