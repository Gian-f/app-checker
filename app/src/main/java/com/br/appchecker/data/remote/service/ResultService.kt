package com.br.appchecker.data.remote.service

import com.br.appchecker.data.remote.request.ChatRequest
import com.br.appchecker.data.remote.response.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ResultService {
    @POST("chat/send")
    @Headers("Content-Type:Application/json")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>

    @POST("chat/receive")
    @Headers("Content-Type:Application/json")
    fun receiveMessage(@Body request: ChatRequest): Call<ChatResponse>
}