package com.kt.myproject.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kt.myproject.base.BaseFragment
import com.kt.myproject.databinding.HomeBinding
import com.kt.myproject.ui.adapter.HomeAdapter
import com.kt.myproject.utils.toast

class HomeFragment : BaseFragment<HomeBinding>() {

    private val list = listOf(
            "Coroutine 1",
            "Coroutine 2",
            "Coroutine 3",
            "Coroutine 4",
            "Coroutine 5",
            "Coroutine 6",
            "Coroutine 7",
            "Coroutine 8",
            "Coroutine 9",
            "Coroutine 10",
            "Coroutine 11"
    )

    private val adapter = HomeAdapter()

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): HomeBinding {
        return HomeBinding.inflate(inflater, viewGroup, false)
    }

    override fun onViewCreated() {
        adapter.set(list)
        adapter.bind(view.homeRecyclerView)
        adapter.onItemClick { s, _ -> toast(s) }
    }

    override fun onLiveDataObserve() {

    }

}