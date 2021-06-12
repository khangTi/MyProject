package com.kt.myproject.ui.fragment.restful

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kt.myproject.repository.api.UserProvider
import com.kt.myproject.repository.model.UserResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RestfulVM : ViewModel() {

    val userLiveData = MutableLiveData<UserResp>()

    val userError = MutableLiveData<Int>()

    fun getUser() {
        Log.e("getUser", "${System.currentTimeMillis()}")
        CoroutineScope(Dispatchers.IO).launch {
            UserProvider.getListUser().collect {
                if (it.code == 200) {
                    userLiveData.postValue(it.data)
                } else {
                    userError.postValue(it.code)
                }
            }
        }
    }

}