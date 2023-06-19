package com.br.appchecker.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("nome")
    val nome: String
)