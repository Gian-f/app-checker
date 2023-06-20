package com.br.appchecker.data.remote.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email:String,
    @SerializedName("senha")
    val password:String
)
