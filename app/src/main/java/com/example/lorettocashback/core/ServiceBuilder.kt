package com.example.lorettocashback.core

import android.util.Log
import com.example.lorettocashback.core.UnsafeOkHttpClient.UnsafeOkHttpClient
import com.example.lorettocashback.core.UnsafeOkHttpClient.UnsafeOkHttpClientWithInterceptor
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.remote.services.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {



    inline fun <reified S> createService(): S {
        return getRetrofitInstance().create(S::class.java)
    }

    fun createLoginService(): LoginService {
        Log.d("USER_SERVICE", "LOGIN")
        return getRetrofitInstanceForLogin().create(LoginService::class.java)
    }


//    fun <T> createCashbackService(serviceType: Class<T>): T {
//        return getRetrofitInstanceForCashback().create(serviceType)
//    }

    fun <T> createService(serviceType: Class<T>): T {
        Log.d(
            "USER_SERVICE",
            "${serviceType.name} // ${Preferences.sessionID}"
        )
        return getRetrofitInstance().create(serviceType)
    }

    fun getRetrofitInstanceForLogin(): Retrofit {
        val BASE_URL: String = "https://${Preferences.ipAddress.toString()}:${Preferences.portNumber.toString()}/b1s/v2/"
        Log.wtf("base_url", BASE_URL)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(UnsafeOkHttpClient)
            .build()
    }

    fun getRetrofitInstance(): Retrofit {
        Log.d("USER_SERVICE", "INSIDE RETROFIT")
        val BASE_URL: String = "https://${Preferences.ipAddress.toString()}:${Preferences.portNumber.toString()}/b1s/v2/"
        Log.wtf("base_url", BASE_URL.toString())

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(UnsafeOkHttpClientWithInterceptor)
            .build()
    }

    fun getRetrofitInstanceForCashback(): Retrofit {
        val BASE_URL: String = "http://185.65.202.40:3222/"
        Log.wtf("base_url", BASE_URL.toString())

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.client(UnsafeOkHttpClientWithInterceptorForCashback)
            .build()
    }
}

