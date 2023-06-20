package com.br.appchecker.data.remote.request

import com.google.gson.annotations.SerializedName

data class QuestionRequest(
    @SerializedName("codigo_usuario")
    val codigoUsuario: Int,
    @SerializedName("limite")
    val limite: Int = 4,
)
