package com.kt.myproject.repository.model

import com.google.gson.annotations.SerializedName

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class User {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("avatar")
    var avatar: String = ""

    constructor(id: Int, name: String, email: String, avatar: String) {
        this.id = id
        this.name = name
        this.email = email
        this.avatar = avatar
    }

}

class UserResp{

    var listUser : List<User>? = null

    constructor(listUser : List<User>?){
        this.listUser = listUser
    }

}