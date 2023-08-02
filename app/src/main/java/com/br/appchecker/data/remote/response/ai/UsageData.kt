package com.br.appchecker.data.remote.response.ai

import com.google.gson.annotations.SerializedName

data class UsageData(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int
)
