package com.kt.myproject.repository.model

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RepeatData {

    var actor: String = ""
    var image: String = ""
    var date: String = ""

    constructor(actor: String, image: String, date: String) {
        this.actor = actor
        this.image = image
        this.date = date
    }

}