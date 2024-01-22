package com.example.lorettocashback.core

import android.util.Log
import com.example.lorettocashback.data.Preferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val sessionId = Preferences.sessionID
        val companyDB = Preferences.companyDB

        val request: Request

        Log.d("USER_SERVICE_AUTH", sessionId.toString())

        request = if (sessionId != null && companyDB != null) {
            chain.request()
                .newBuilder()
                .addHeader("Cookie", "B1SESSION=$sessionId")
                .addHeader("Cookie", "CompanyDB=$companyDB")
                .build()
        } else chain.request()

        return chain.proceed(request)
    }


}