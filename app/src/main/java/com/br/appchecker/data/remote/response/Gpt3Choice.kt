package com.br.appchecker.data.remote.response

data class Gpt3Choice(
    val text: String,
    val finish_reason: String?
)