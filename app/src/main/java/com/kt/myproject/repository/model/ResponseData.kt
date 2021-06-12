package com.kt.myproject.repository.model

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ResponseData<T> {

    var code: Int = 0
    var status: String = ""
    var data: T? = null

    constructor(code: Int, status: String, data: T?) {
        this.code = code
        this.status = status
        this.data = data
    }

    constructor()

}