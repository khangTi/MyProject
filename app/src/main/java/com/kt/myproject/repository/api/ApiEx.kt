package com.kt.myproject.repository.api

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.kt.myproject.repository.model.ApiResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/08/24
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Response<ResponseBody>.handlerResp(): ApiResponse<JsonArray> {
    val resp = this@handlerResp
    val result = ApiResponse<JsonArray>()
    if (resp.code() != 200) {
        result.apply {
            code = resp.code()
            message = resp.message()
        }
        return result
    }
    try {
        val json = resp.body()?.string() ?: ""
        val body = Gson().fromJson(json, JsonArray::class.java)
        result.apply {
            code = resp.code()
            message = resp.message()
            data = body
        }
        return result
    } catch (e: Exception) {
        result.apply {
            code = -1
            message = "convert data fail"
        }
        return result
    }
}

fun <T> java.lang.Exception.handlerApi(): ApiResponse<T> {
    return when (this) {
        is HttpException, is UnknownHostException, is SocketTimeoutException -> {
            val api = ApiResponse<T>().apply {
                code = -1
                message = this.message
            }
            api
        }
        else -> {
            val api = ApiResponse<T>().apply {
                code = -100
                message = this.message
            }
            api
        }
    }
}