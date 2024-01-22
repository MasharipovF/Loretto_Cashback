package com.example.lorettocashback.domain.interactor

import android.util.Log
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.userdefaults.UserDefaults
import com.example.lorettocashback.data.repository.LoginRepository
import com.example.lorettocashback.data.repository.LoginRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse
import com.example.lorettocashback.domain.dto.login.LoginResponseDto

interface LoginInteractor {
    suspend fun requestLogin(): Boolean
    var errorMessage: String?
}

class LoginInteractorImpl() : LoginInteractor {

    private val repository: LoginRepository by lazy { LoginRepositoryImpl() }

    override var errorMessage: String? = null

    override suspend fun requestLogin(
    ): Boolean {
        val response = repository.requestLogin()
        return if (response is LoginResponseDto) {
            Preferences.sessionID = response.SessionId
            Preferences.companyDB = GeneralConsts.COMPANY_DB
            Preferences.userName = GeneralConsts.LOGIN
            Preferences.userPassword = GeneralConsts.PASSWORD


            Log.d("USER_INTERACTOR", "sessionId is ${response.SessionId}")

            //KLIENT KIRITGAN LOGIN PAROL BOYICHA QIDIRISH KERAK
            val usrDefaults = repository.getUserDefaults("username")
            if (usrDefaults is UserDefaults) {
                //Preferences.userCode = usrDefaults.value[0].users.userCode
                /* KLIENT ISMI
                PAROLI
                TELEFONI
                 */

            } else {
                errorMessage = (usrDefaults as ErrorResponse).error.message.value
                return false
            }

            true
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            false
        }
    }
}