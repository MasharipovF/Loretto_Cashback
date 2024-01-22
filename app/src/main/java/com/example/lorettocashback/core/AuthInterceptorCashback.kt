package com.example.lorettocashback.core

import android.util.Log
import com.example.lorettocashback.data.Preferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptorCashback() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val sessionId = Preferences.cashbackToken

        val request: Request

        Log.d("USER_SERVICE_AUTH_CASHBACK", sessionId.toString())

        request = if (sessionId != null ) {
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Token $sessionId")
                .build()
        } else chain.request()

        return chain.proceed(request)
    }


}