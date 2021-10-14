package com.kt.myproject.ui.activity

import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.kt.myproject.base.BaseViewModel
import com.kt.myproject.base.EventLiveData
import com.kt.myproject.ex.toast
import com.kt.myproject.repository.store.PreferencesStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/10/14
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MainVM : BaseViewModel() {

    init {
        fetchToken()
        eventTokenShared()
    }

    val tokenMessageEvent = EventLiveData<String>()

    private fun fetchToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            tokenMessageEvent.postValue(it)
        }.addOnFailureListener {
            toast(it.message.toString())
        }
    }

    private fun eventTokenShared() {
        PreferencesStore.token.onEach {
            toast(it)
        }.launchIn(viewModelScope)
    }

}