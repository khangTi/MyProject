package com.kt.myproject.repository.api

import com.kt.myproject.repository.model.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface ApiService {

    @GET("users")
    suspend fun getUsers(): Response<ResponseBody>

    @GET("more-users")
    suspend fun getMoreUser(): List<User>

    @GET("error")
    suspend fun getUsersWithError(): List<User>

}