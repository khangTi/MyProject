package com.kt.myproject.base

import android.view.View
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.kt.myproject.R

interface BaseView {

    fun onViewClick(v: View?) {}

    val nav: NavController

    fun navigate(directions: NavDirections, block: (NavOptions.Builder.() -> Unit) = {}) {
        val options = NavOptions.Builder()
        options.block()
        nav.navigate(directions, options.build())
    }

    fun navigateUp() {
        nav.navigateUp()
    }

    fun NavOptions.Builder.setDefaultAnim(): NavOptions.Builder {
        setEnterAnim(R.anim.vertical_reserved_enter)
        setPopEnterAnim(R.anim.vertical_reserved_pop_enter)
        return this
    }

    fun NavOptions.Builder.setNoneAnim(): NavOptions.Builder {
        setEnterAnim(0)
        setPopEnterAnim(0)
        return this
    }

    fun NavOptions.Builder.setLaunchSingleTop(): NavOptions.Builder {
        setLaunchSingleTop(true)
        setPopUpTo(nav.graph.id, false)
        return this
    }

}