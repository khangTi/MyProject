package com.kt.myproject.base

import com.kt.myproject.NavigationDirections
import com.kt.myproject.ui.fragment.call.CallFragmentDirections

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object NavigationId {

    val home = NavigationDirections.actionGlobalHomeFragment()

    val repeat = NavigationDirections.actionGlobalRepeatFragment()

    val restful = NavigationDirections.actionGlobalRestfulFragment()

    val cartDialog = NavigationDirections.actionGlobalCartDialog()

    val notification = NavigationDirections.actionGlobalNotificationFragment()

    val adv = NavigationDirections.actionGlobalSlideFragment()

    val preferences = NavigationDirections.actionGlobalPreferencesFragment()

    val gallery = NavigationDirections.actionGlobalGalleryFragment()

    val realtime = NavigationDirections.actionGlobalRealtimeFragment()

    val swipe = NavigationDirections.actionGlobalSwipeFragment()

    val bottom = NavigationDirections.actionGlobalBottomSheetFragment()

    val image = NavigationDirections.actionGlobalImageFragment()

    val camera = NavigationDirections.actionGlobalCameraFragment()

    val mask = NavigationDirections.actionGlobalMaskFaceFragment()

    val mask2 = NavigationDirections.actionGlobalMask2Fragment()

    val range = NavigationDirections.actionGlobalRangeFragment()

    val button = NavigationDirections.actionGlobalButtonFragment()

    /**
     * Call Directions
     */
    val call = CallFragmentDirections.actionGlobalCallFragment()

}