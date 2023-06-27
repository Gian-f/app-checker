package com.br.appchecker.util

import com.br.appchecker.data.remote.response.ErrorResponse
import com.google.gson.Gson

object ErrorUtils {
    //Função responsável por implementar o parser do errorBody de requisições
    fun parseErrorMessage(errorBody: String): String {
        val gson = Gson()
        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
        return errorResponse.message ?: "Ocorreu um erro no servidor."
    }
}