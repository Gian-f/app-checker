package com.br.appchecker.data.remote.request

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("message")
    val message: String
)
