package com.kt.myproject.ui.fragment.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.myproject.base.EventLiveData
import com.kt.myproject.repository.FirebaseRepository
import com.kt.myproject.repository.model.RealtimeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/08/27
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RealtimeVM : ViewModel() {

    val realtimeFailedEvent = EventLiveData<String>()

    val realtimeDataEvent = EventLiveData<RealtimeData>()

    fun eventRealtime() {
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseRepository.eventRealtime()
                .catch { error ->
                    realtimeFailedEvent.postValue(error.message)
                }
                .collect {
                    realtimeDataEvent.postValue(it)
                }
        }
    }

}