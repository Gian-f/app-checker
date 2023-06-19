package com.br.appchecker.data.remote.service

import com.br.appchecker.data.remote.response.QuestionResponse
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestionService {
    @GET("/question/items/:id")
    suspend fun getAllQuestionsById(
        @Path("id")
        idUser: String
    ): List<QuestionResponse>

    @GET("/question/items")
    suspend fun getAllQuestions(): List<QuestionResponse>

    @POST("/question")
    suspend fun insertQuestion(): QuestionResponse
}
