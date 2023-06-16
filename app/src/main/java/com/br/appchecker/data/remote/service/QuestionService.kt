package com.br.appchecker.data.remote.service

import android.util.Log
import com.br.appchecker.data.remote.config.HttpClientConfig
import com.br.appchecker.data.remote.response.QuestionResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class QuestionService {

    private val BASE_URL = "http://localhost:8080"

    suspend fun getAll(): List<QuestionResponse> {
        val response = HttpClientConfig.client.get("$BASE_URL/question")
        Log.d("TAG", "${response.status} - {$response.request}")
        return response.body()
    }

    suspend fun insert(): QuestionResponse {
        val response = HttpClientConfig.client.post("$BASE_URL/question")
        Log.d("TAG", "${response.status} - {$response.request}")
        return response.body()
    }

}
