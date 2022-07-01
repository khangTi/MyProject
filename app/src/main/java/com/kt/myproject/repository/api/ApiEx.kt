package com.kt.myproject.repository.api

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.kt.myproject.ex.int
import com.kt.myproject.ex.str
import com.kt.myproject.ex.toJsonObject
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
inline fun <reified T> Gson.fromJson(json: String?): T =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Response<ResponseBody>.handleResp(): ApiResponse<T> {
    val resp = this@handleResp
    val result = ApiResponse<T>()
    if (resp.code() != 200) {
        result.apply {
            code = resp.code()
            message = resp.message()
        }
        return result
    }
    try {
        val json = resp.body()?.string()
        val resp = Gson().fromJson<T>(json)
        result.apply {
            data = resp
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

inline fun <T> java.lang.Exception.handlerApi(): ApiResponse<T> {
    return when (this) {
        is HttpException, is UnknownHostException, is SocketTimeoutException -> {
            val api = ApiResponse<T>().apply {
                code = -1
                message = this@handlerApi.message.toString()
            }
            api
        }
        else -> {
            val api = ApiResponse<T>().apply {
                code = -100
                message = this@handlerApi.message.toString()
            }
            api
        }
    }
}