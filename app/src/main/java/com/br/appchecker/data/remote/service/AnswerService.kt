package com.br.appchecker.data.remote.service

import com.br.appchecker.data.remote.request.AnswerRequest
import com.br.appchecker.data.remote.response.AnswersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnswerService {
     @POST("/resposta")
     @Headers("Content-Type:Application/json")
     fun createAnswer(@Body answerRequest: AnswerRequest) : Call<AnswersResponse>
}