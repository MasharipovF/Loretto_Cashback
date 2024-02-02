package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.data.entity.businesspartners.CashbackUsersVal
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCodeVal
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QrCodeService {
    companion object {

        fun get(): QrCodeService =
            ServiceBuilder.createService(QrCodeService::class.java)
    }

    @GET("sml.svc/CASHBACK_ELIGIBLE_ITEMS")
    suspend fun getUserQrCodeData(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$filter") filter: String? = null,
    ): Response<CashbackQrCodeVal>
}