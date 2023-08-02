package com.br.appchecker.data.remote.request

import com.br.appchecker.domain.model.Message

data class ChatRequest(
    val messages: List<Message>,
    val model: String = "gpt-3.5-turbo"
)