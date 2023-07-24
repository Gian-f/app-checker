package com.br.appchecker.data.repository.ai

import com.br.appchecker.data.state.StateInfo
import com.br.appchecker.domain.model.Question

interface ResultRepository {

    suspend fun sendMessage(message: String, questions: List<Question>): StateInfo<String>
}