package com.kt.myproject.ex

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Activity.getIpAdress(): String {
    val vm = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return Formatter.formatIpAddress(vm.connectionInfo.ipAddress)
}