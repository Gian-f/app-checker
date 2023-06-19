package com.br.appchecker.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuestionData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("deleted")
    val deleted: String
)
