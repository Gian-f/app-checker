package com.br.appchecker.data.repository.answer

import com.br.appchecker.data.remote.request.AnswerRequest
import com.br.appchecker.data.state.StateInfo

interface AnswerRepository {
    suspend fun insertAnswer(
        questionOption: Int,
        questionUser: Int,
        questionCode: Int
    ): StateInfo<AnswerRequest>
}