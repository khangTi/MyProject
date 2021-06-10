package com.kt.myproject.app

import android.app.Application

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

lateinit var app : MyApp

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

}