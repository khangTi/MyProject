package com.kt.myproject.utils.port

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/07/12
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ScanHost {

    private var delegate : MainAsyncResponse? = null

    private var ip : String? = null

    constructor(l : MainAsyncResponse, ip : String){
        this.delegate = l
        this.ip = ip
    }



}