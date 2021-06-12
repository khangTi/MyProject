package com.kt.myproject.repository.api

import com.kt.myproject.repository.model.ResponseData
import com.kt.myproject.repository.model.User
import com.kt.myproject.repository.model.UserResp
import com.kt.myproject.utils.toArray
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object UserProvider {

    fun getListUser() = flow {
        val data = ResponseData<UserResp>()
        val resp = RetrofitBuilder.apiService.getUsers()
        if (!resp.isSuccessful || resp.code() != 200) {
            data.apply {
                code = resp.code()
                status = resp.message()
            }
            emit(data)
            return@flow
        }
        val body = resp.body()?.string().apply {
            this ?: data.apply {
                code = resp.code()
                status = resp.message()
            }
        } ?: return@flow
        data.apply {
            this.code = resp.code()
            this.status = resp.message()
            this.data = UserResp(body.toArray(User::class.java))
        }
        emit(data)
    }


}