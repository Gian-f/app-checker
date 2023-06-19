package com.br.appchecker.data.remote.response

import com.br.appchecker.data.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("info")
    val info: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("pergunta")
    val permission: String,
    @SerializedName("status")
    val status: Boolean
)