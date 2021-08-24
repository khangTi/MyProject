package com.kt.myproject.repository

import com.kt.myproject.ex.parse
import com.kt.myproject.ex.toObject
import com.kt.myproject.repository.api.RetrofitBuilder
import com.kt.myproject.repository.api.handlerApi
import com.kt.myproject.repository.api.handlerResp
import com.kt.myproject.repository.model.ApiResponse
import com.kt.myproject.repository.model.User
import com.kt.myproject.repository.model.UserResp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object UserRepository {

    suspend fun getListUser(): Flow<ApiResponse<List<User>>> = flow {
        try {

            val resp = RetrofitBuilder.apiService.getUsers().handlerResp()
            val userResp = ApiResponse<List<User>>(resp.code, resp.message)
            when (resp.code == 200) {
                true -> {
                    val listUser = mutableListOf<User>()
                    resp.data?.forEach {
                        it.toObject().parse(User::class.java)
                            ?.let { it1 -> listUser.add(it1) }
                    }
                    userResp.apply {
                        code = 0
                        message = ""
                        data = listUser
                    }
                    emit(userResp)
                }
                else -> {
                    emit(userResp)
                }
            }
            java.lang.Exception().handlerApi<List<UserResp>>()
        } catch (e: Exception) {
            e.handlerApi<List<User>>()
        }
    }


}