package com.kt.myproject.ui.fragment.restful

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kt.myproject.base.BaseViewModel
import com.kt.myproject.repository.UserRepository
import com.kt.myproject.repository.model.User
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
class RestfulVM : BaseViewModel() {

    val userLiveData = MutableLiveData<List<User>>()

    val userError = MutableLiveData<Int>()

    fun getUser() {
        Log.e("getUser", "${System.currentTimeMillis()}")
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.getListUser().collect {
                if (it.code == 0) {
                    userLiveData.postValue(it.data)
                } else {
                    userError.postValue(it.code)
                }
            }
        }
    }

}