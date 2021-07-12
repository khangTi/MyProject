package com.kt.myproject.ui.fragment.port

data class PortData(
    var ssid: String,
    var bssid: String,
    var capabilities: String,
    var freqiency: Int,
    var level: String,
    var ipAddress: String
)