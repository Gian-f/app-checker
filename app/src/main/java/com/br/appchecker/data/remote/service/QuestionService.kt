package com.br.appchecker.data.remote.service

import com.br.appchecker.data.remote.response.QuestionResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface QuestionService {
    @GET("/question")
    suspend fun getQuestions(): List<QuestionResponse>

    @POST("/question")
    suspend fun insertQuestion(): QuestionResponse
    }
