package com.kt.myproject.repository.model

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/08/24
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ApiResponse<T> {

    var code: Int = -1

    var data: T? = null

    var message: String? = ""

    constructor()

    constructor(code : Int, message: String?){
        this.code = code
        this.message = message
    }

    constructor(code: Int, data: T?, message: String?) {
        this.code = code
        this.data = data
        this.message = message
    }

}