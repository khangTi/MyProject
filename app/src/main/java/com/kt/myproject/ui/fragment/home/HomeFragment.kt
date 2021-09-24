package com.kt.myproject.ui.fragment.home

import com.kt.myproject.base.BaseFragment
import com.kt.myproject.base.NavigationId
import com.kt.myproject.databinding.HomeBinding
import com.kt.myproject.repository.data.UI
import com.kt.myproject.repository.data.list
import com.kt.myproject.ui.activity.CallActivity
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
        adapter.onItemClick = { s, _ -> navigationUI(s) }
    }

    private fun navigationUI(s: UI) {
        when (s) {
            UI.ADV -> navigate(NavigationId.adv)
            UI.MASK -> navigate(NavigationId.mask)
            UI.MASK2 -> navigate(NavigationId.mask2)
            UI.CAMERA -> navigate(NavigationId.camera)
            UI.GALLERY -> navigate(NavigationId.gallery)
            UI.CUSTOM_IMAGE -> navigate(NavigationId.image)
            UI.CALL -> startClear(CallActivity::class.java)
            UI.BOTTOM_SHEET -> navigate(NavigationId.bottom)
            UI.SWIPE_RECYCLER -> navigate(NavigationId.swipe)
            UI.BASIC_COROUTINE -> navigate(NavigationId.repeat)
            UI.NOTIFY_LIST -> navigate(NavigationId.notification)
            UI.REALTIME_FIREBASE -> navigate(NavigationId.realtime)
            UI.RETROFIT_COROUTINE -> navigate(NavigationId.restful)
            UI.PREFERENCES_DATA_STORE -> navigate(NavigationId.preferences)
        }
    }

}