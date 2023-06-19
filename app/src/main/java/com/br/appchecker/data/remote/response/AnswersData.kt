package com.br.appchecker.data.remote.response

import com.google.gson.annotations.SerializedName

data class AnswersData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("idQuestion")
    val idQuestion: Int,
    @SerializedName("positionOrder")
    val positionOrder: Int
) {
    override fun toString(): String {
        return description
    }
}