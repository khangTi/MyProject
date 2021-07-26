package com.kt.myproject.ui.fragment.home

import com.kt.myproject.base.BaseFragment
import com.kt.myproject.base.NavigationId
import com.kt.myproject.databinding.HomeBinding
import com.kt.myproject.repository.data.UI
import com.kt.myproject.repository.data.list
import com.kt.myproject.ui.adapter.HomeAdapter

class HomeFragment : BaseFragment<HomeBinding>(HomeBinding::inflate) {

    private val adapter = HomeAdapter()

    override fun onViewCreated() {
        setupAdapter()
    }

    override fun onLiveDataObserve() {}

    private fun setupAdapter() {
        adapter.set(list)
        adapter.bind(binding.homeRecyclerView)
        adapter.onItemClick { s, _ -> navigationUI(s) }
    }

    private fun navigationUI(s: UI) {
        when (s) {
            UI.BASIC_COROUTINE -> navigate(NavigationId.repeat)
            UI.RETROFIT_COROUTINE -> navigate(NavigationId.restful)
            UI.NOTIFY_LIST -> navigate(NavigationId.notification)
            UI.SLIDE -> navigate(NavigationId.floating)
            UI.PREFERENCES_DATA_STORE -> navigate(NavigationId.preferences)
        }
    }

}