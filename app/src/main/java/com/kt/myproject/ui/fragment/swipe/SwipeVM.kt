package com.kt.myproject.ui.fragment.swipe

import androidx.lifecycle.viewModelScope
import com.kt.myproject.base.BaseViewModel
import com.kt.myproject.base.EventLiveData
import com.kt.myproject.repository.SampleRepository
import com.kt.myproject.repository.data.CardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/01
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SwipeVM : BaseViewModel() {

    val banksEvent = EventLiveData<List<CardItem>>()

    fun fetchBanks() {
        viewModelScope.launch(Dispatchers.IO) {
            SampleRepository.fetchBanks()
                .catch { e -> log.d(e.message) }
                .collect {
                    banksEvent.postValue(it)
                }
        }
    }

}