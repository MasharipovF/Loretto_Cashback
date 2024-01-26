package com.example.lorettocashback.domain.interactor

import android.util.Log
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.businesspartners.BusinessPartners
import com.example.lorettocashback.data.entity.businesspartners.BusinessPartnersVal
import com.example.lorettocashback.data.repository.BpRepository
import com.example.lorettocashback.data.repository.BpRepositoryImpl
import com.example.lorettocashback.data.repository.LoginRepository
import com.example.lorettocashback.data.repository.LoginRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse
import com.example.lorettocashback.domain.dto.login.LoginResponseDto

interface LoginInteractor {
    suspend fun requestLogin(phone: String, password: String): BusinessPartners?
    var errorMessage: String?
}

class LoginInteractorImpl() : LoginInteractor {

    private val repository: LoginRepository by lazy { LoginRepositoryImpl() }
    private val bpRepository: BpRepository by lazy {BpRepositoryImpl() }

    override var errorMessage: String? = null

    override suspend fun requestLogin(
        phone: String,
        password: String,
    ): BusinessPartners? {

        val response = repository.requestLogin()

        if (response is LoginResponseDto) {
            Preferences.sessionID = response.SessionId
            Preferences.companyDB = GeneralConsts.COMPANY_DB
            Preferences.userName = GeneralConsts.LOGIN
            Preferences.userPassword = GeneralConsts.PASSWORD

            Log.d("USER_INTERACTOR", "sessionId is ${response.SessionId}")

            //KLIENT KIRITGAN LOGIN PAROL BOYICHA QIDIRISH KERAK
            val userResponse = bpRepository.getUserData(phone, password)

            if (userResponse is BusinessPartnersVal) {

                if (!userResponse.value.isNullOrEmpty()){
                    Preferences.cardName = userResponse.value[0].CardName
                    return userResponse.value[0]
                } else {
                    errorMessage = "Бизнес партнер с кодом $phone не найден!"
                    return  null
                }

            } else {
                errorMessage = (userResponse as ErrorResponse).error.message.value
                return null
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
             return null
        }
    }
}