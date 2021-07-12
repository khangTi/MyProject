package com.kt.myproject.utils.port

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log




/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NetworkReceiver(mainWifi : WifiManager): BroadcastReceiver() {

    private val wifi : WifiManager = mainWifi

    override fun onReceive(context: Context?, intent: Intent?) {
        val sb = StringBuilder()
        val csv = StringBuilder()
        val wifiList = wifi.scanResults

        // prepare text for display and CSV table

        // prepare text for display and CSV table
        sb.append("Number of APs Detected: ")
        sb.append(Integer.valueOf(wifiList.size).toString())
        sb.append("\n\n")
        for (i in 0 until wifiList.size) {
            // sb.append((Integer.valueOf(i + 1)).toString() + ".");
            // SSID
            sb.append("SSID:").append(wifiList.get(i).SSID)
            sb.append("\n")
            csv.append(wifiList.get(i).SSID)
            csv.append(",")

            // BSSID
            sb.append("BSSID:").append(wifiList.get(i).BSSID)
            sb.append("\n")
            csv.append(wifiList.get(i).BSSID)
            csv.append(",")
            // capabilities
            sb.append("Capabilities:").append(
                wifiList.get(0).capabilities
            )
            sb.append("\n")
            // frequency
            sb.append("Frequency:").append(wifiList.get(i).frequency)
            sb.append("\n")
            csv.append(wifiList.get(i).frequency)
            csv.append(",")
            // level
            sb.append("Level:").append(wifiList.get(i).level)
            sb.append("\n\n")
            csv.append(wifiList.get(i).level)
            csv.append("\n")
        }
        Log.e("dssadsads", sb.toString())
    }

}