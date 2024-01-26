package com.example.lorettocashback.data.repository

import android.util.Log
import com.example.lorettocashback.core.ErrorCodeEnums
import com.example.lorettocashback.data.remote.services.BusinessPartnersService
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.LoginUtils.reLogin
import com.example.lorettocashback.util.retryIO

interface BpRepository {

    suspend fun getUserData(phone: String, password: String): Any? //BusinessPartners?

}

class BpRepositoryImpl(
    private val bpService: BusinessPartnersService = BusinessPartnersService.get(),
) :
    BpRepository {
    override suspend fun getUserData(phone: String, password: String): Any? {
        val response = retryIO {
            val filterString = "Password eq '$password' and contains(Phone, '$phone')"

            bpService.getUserData(
                filter = filterString
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error.error.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getUserData(phone, password)
                else return error

            } else return error
        }
    }

}