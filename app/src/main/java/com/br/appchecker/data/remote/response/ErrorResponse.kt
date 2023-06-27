package com.br.appchecker.data.remote.response

data class ErrorResponse(
    val message: String? = null,
    val details: String? = null,
    val timestamp: String? = null
)