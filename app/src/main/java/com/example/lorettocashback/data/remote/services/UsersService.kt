package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.data.entity.userdefaults.UserDefaultsResponseVal
import retrofit2.Response
import retrofit2.http.*

interface UsersService {

    companion object {

        fun get(): UsersService = ServiceBuilder.createService(UsersService::class.java)
    }


    @GET("sml.svc/USER_DEFAULTS")
    suspend fun getUserDefaults(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500000",
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$filter") userCode: String,
    ): Response<UserDefaultsResponseVal>

    /*
    @POST("QueryService_PostQuery")
    suspend fun getUserDefaults(@Body body: CrossJoin): Response<UserDefaults>*/
}