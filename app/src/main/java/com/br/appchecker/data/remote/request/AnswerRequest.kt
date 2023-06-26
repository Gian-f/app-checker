package com.br.appchecker.data.remote.request

import com.google.gson.annotations.SerializedName

data class AnswerRequest(
   @SerializedName("codigo_pergunta")
    val questionCode: Int,
   @SerializedName("codigo_opcao")
    val questionOption: Int,
   @SerializedName("codigo_usuario")
    val questionUser: Int,
)
