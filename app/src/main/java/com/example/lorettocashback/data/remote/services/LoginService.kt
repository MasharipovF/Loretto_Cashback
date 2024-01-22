package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.domain.dto.login.LoginRequestDto
import com.example.lorettocashback.domain.dto.login.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    companion object{
        fun get(): LoginService = ServiceBuilder.createLoginService()
    }

    @POST("Login")
    suspend fun requestLogin(@Body body: LoginRequestDto): Response<LoginResponseDto>

}