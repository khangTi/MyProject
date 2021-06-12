package com.kt.myproject.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object RetrofitBuilder {

    private const val URL = "https://5e510330f2c0d300147c034c.mockapi.io/"

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = retrofit().create(ApiService::class.java)

}