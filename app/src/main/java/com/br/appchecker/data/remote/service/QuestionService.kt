package com.br.appchecker.data.remote.service

import com.br.appchecker.data.remote.request.QuestionRequest
import com.br.appchecker.data.remote.response.QuestionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QuestionService {
    @POST("/question/items")
    suspend fun getAllQuestions(
        @Body questionRequest: QuestionRequest
    ): List<QuestionResponse>

//    @GET("/question/items")
//    suspend fun getAllQuestions(): List<QuestionResponse>

    @POST("/question")
    suspend fun insertQuestion(): QuestionResponse
}
