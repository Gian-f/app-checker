package com.br.appchecker.data.remote.config

import com.br.appchecker.data.remote.service.AnswerService
import com.br.appchecker.data.remote.service.LoginService
import com.br.appchecker.data.remote.service.QuestionService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiServiceFactory {

    private const val BASE_URL = "http://192.168.203.56:8081"

    fun createQuestionService(): QuestionService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(QuestionService::class.java)
    }

    fun createLoginService(): LoginService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(LoginService::class.java)
    }

    fun createAnswerService(): AnswerService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(AnswerService::class.java)
    }

    fun isServerOnline(serverUrl: String): Boolean {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(serverUrl)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }

}