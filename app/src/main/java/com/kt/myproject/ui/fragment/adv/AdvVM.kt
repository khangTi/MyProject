package com.kt.myproject.ui.fragment.adv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.myproject.R
import com.kt.myproject.base.EventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/08/24
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AdvVM : ViewModel() {

    val imagesLiveData = MutableLiveData<List<AdvItem>>()

    val pageLiveData = EventLiveData<Boolean>()

    private var jobCountdown: Job? = null

    fun fetchAdvList() {
        imagesLiveData.postValue(
            listOf(
                AdvItem(imageRes = R.mipmap.img_adv1),
                AdvItem(imageRes = R.mipmap.img_adv2),
                AdvItem(imageRes = R.mipmap.img_adv3),
                AdvItem(imageRes = R.mipmap.img_adv4),
                AdvItem(imageRes = R.mipmap.img_adv5),
                AdvItem(imageRes = R.mipmap.img_adv6)
            )
        )
    }

    fun countdownToNextSlide() {
        jobCountdown?.cancel()
        jobCountdown = viewModelScope.launch {
            delay(5000)
            pageLiveData.postValue(true)
        }
    }

}