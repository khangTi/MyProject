package com.kt.myproject.repository

import com.google.gson.JsonArray
import com.kt.myproject.ex.parse
import com.kt.myproject.ex.readAsset
import com.kt.myproject.ex.toObject
import com.kt.myproject.repository.data.CardItem
import kotlinx.coroutines.flow.flow

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/01
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object SampleRepository {

    fun fetchBanks() = flow {
        try {
            val jsonArray = "banks.json".readAsset().parse(JsonArray::class.java)
            val banks = mutableListOf<CardItem>()
            jsonArray?.forEach {
                val bank = it.toObject().parse(CardItem::class.java) ?: CardItem()
                banks.add(bank)
            }
            banks.sortWith { o1, o2 -> o1.shortName[0].compareTo(o2.shortName[0]) }
            emit(banks)
        } catch (e: Exception) {
            throw e
        }
    }

}