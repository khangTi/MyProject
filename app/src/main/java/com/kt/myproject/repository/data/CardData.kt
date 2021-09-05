package com.kt.myproject.repository.data

import com.google.gson.annotations.SerializedName

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/01
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
data class CardItem(
    @SerializedName("BankCode")
    var bankCode: String = "",

    @SerializedName("BankName")
    var bankName: String = "",

    @SerializedName("Descriptions")
    var descriptions: String = "",

    @SerializedName("BackgroundColors")
    var colors: List<String> = listOf("#512879", "#7B4FA4"),

    @SerializedName("BankShortName")
    var shortName: String = "",

    @SerializedName("IsDisable")
    var isDisable: Boolean = false,
)