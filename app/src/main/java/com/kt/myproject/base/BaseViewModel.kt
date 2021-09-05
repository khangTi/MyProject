package com.kt.myproject.base

import androidx.lifecycle.ViewModel
import com.kt.myproject.utils.Logger

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/09/01
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseViewModel : ViewModel() {

    val log by lazy {
        Logger(this.javaClass.simpleName)
    }

}
