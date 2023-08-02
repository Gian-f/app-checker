package com.br.appchecker.data.remote.response.ai

import com.google.gson.annotations.SerializedName


data class ChatResponse(
    val id: String,
    @SerializedName("object")
    val obj: String,
    @SerializedName("created")
    val created: Long,
    @SerializedName("choices")
    val choices: List<Choices>,
    @SerializedName("usage")
    val usageData: UsageData
)