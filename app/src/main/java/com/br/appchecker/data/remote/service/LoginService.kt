package com.br.appchecker.data.remote.service

import com.br.appchecker.data.remote.request.LoginRequest
import com.br.appchecker.data.remote.request.UserRequest
import com.br.appchecker.data.remote.response.LoginResponse
import com.br.appchecker.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @POST("/user/login")
    @Headers("Content-Type:Application/json")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/user")
    @Headers("Content-Type:Application/json")
    fun createUser(@Body userRequest: UserRequest): Call<UserResponse>
}