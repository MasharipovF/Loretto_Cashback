package com.example.lorettocashback.util

import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.remote.services.LoginService
import com.example.lorettocashback.domain.dto.login.LoginRequestDto

object LoginUtils {

    private val loginService: LoginService = LoginService.get()

    suspend fun reLogin(): Boolean {
        val response = retryIO {
            loginService.requestLogin(
                LoginRequestDto(
                    Preferences.companyDB,
                    Preferences.userPassword,
                    Preferences.userName
                )
            )
        }
        return if (response.isSuccessful) {
            Preferences.sessionID = response.body()?.SessionId
            true
        } else {
            false
        }
    }
}