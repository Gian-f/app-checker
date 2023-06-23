package com.br.appchecker.data.remote.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("email")
    val email:String,
    @SerializedName("senha")
    val password:String,
    @SerializedName("nome")
    val name: String,
)