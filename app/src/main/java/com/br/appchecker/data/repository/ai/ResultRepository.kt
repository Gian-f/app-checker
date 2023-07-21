package com.br.appchecker.data.repository.ai

import com.br.appchecker.data.state.StateInfo

interface ResultRepository {

    suspend fun sendMessage(message: String): StateInfo<String>

    suspend fun receiveMessage(message: String): StateInfo<String>
}