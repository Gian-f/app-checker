package com.br.appchecker.data.remote.response.ai

import com.google.gson.annotations.SerializedName

data class Choices(
    val index: Int,
    @SerializedName("message")
    val messageData: MessageData,
    @SerializedName("finish_reason")
    val finishReason: String?
)