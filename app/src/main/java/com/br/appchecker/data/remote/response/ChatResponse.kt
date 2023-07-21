package com.br.appchecker.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("message")
    val message: String
)
