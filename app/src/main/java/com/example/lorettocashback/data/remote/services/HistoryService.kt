package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.data.entity.history.CashbackAmountVal
import com.example.lorettocashback.data.entity.history.CashbackHistoryVal
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HistoryService {
    companion object {
        fun get(): HistoryService =
            ServiceBuilder.createService(HistoryService::class.java)
    }

    @GET("sml.svc/CASHBACK_HISTORY")
    suspend fun getUserHistoryData(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$filter") filter: String? = null,
        @Query("\$skip") skipValue: Int? = null,
    ): Response<CashbackHistoryVal>

    @GET("sml.svc/CASHBACK_HISTORY")
    suspend fun getTotalSum(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Header("Prefer") maxPageSize: String = GeneralConsts.RETURN_ALL_DATA,
        @Query("\$apply") applyGroupBy: String? = null,
        @Query("\$apply") applyAggregate: String? = null,
        @Query("\$filter") filter: String? = null,
    ): Response<CashbackAmountVal>
}