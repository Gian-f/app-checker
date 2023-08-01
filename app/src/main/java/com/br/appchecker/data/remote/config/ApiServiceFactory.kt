package com.br.appchecker.data.remote.config

import android.content.Context
import com.br.appchecker.R
import com.br.appchecker.data.remote.service.AnswerService
import com.br.appchecker.data.remote.service.LoginService
import com.br.appchecker.data.remote.service.QuestionService
import com.br.appchecker.data.remote.service.ResultService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Properties

object ApiServiceFactory {
    private lateinit var BASE_URL: String
    private lateinit var OPEN_AI_KEY: String

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createQuestionService(context: Context): QuestionService {
        BASE_URL = context.getString(R.string.base_url)
        return retrofit.create(QuestionService::class.java)
    }

    fun createLoginService(context: Context): LoginService {
        BASE_URL = context.getString(R.string.base_url)
        return retrofit.create(LoginService::class.java)
    }

    fun createAnswerService(context: Context): AnswerService {
        BASE_URL = context.getString(R.string.base_url)
        return retrofit.create(AnswerService::class.java)
    }

    fun createResultService(context: Context): ResultService {
        val inputStream = context.resources.openRawResource(R.raw.secrets)
        val properties = Properties()
        properties.load(inputStream)
        OPEN_AI_KEY = properties.getProperty("OPEN_AI_API_KEY")
        BASE_URL = context.getString(R.string.open_ai_base_url)

        val httpClientWithAuth = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $OPEN_AI_KEY")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofitWithAuth = retrofit.newBuilder()
            .baseUrl(BASE_URL)
            .client(httpClientWithAuth)
            .build()

        return retrofitWithAuth.create(ResultService::class.java)
    }
}