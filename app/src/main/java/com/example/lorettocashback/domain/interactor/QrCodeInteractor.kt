package com.example.lorettocashback.domain.interactor

import android.util.Log
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCodeVal
import com.example.lorettocashback.data.entity.qr_code.response.QrCodeResponse
import com.example.lorettocashback.data.repository.QrCodeRepository
import com.example.lorettocashback.data.repository.QrCodeRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse
import com.example.lorettocashback.domain.mappers.Mappers

interface QrCodeInteractor {
    suspend fun getUserQrCode(
        serialNumber: String
    ): CashbackQrCode?

    suspend fun postUserQrCode(qrCodeList: ArrayList<CashbackQrCode>): QrCodeResponse?

    var errorMessage: String?
}

class QrCodeInteractorImpl() : QrCodeInteractor {

    private val qrRepository: QrCodeRepository by lazy { QrCodeRepositoryImpl() }

    override var errorMessage: String? = null
    override suspend fun getUserQrCode(serialNumber: String): CashbackQrCode? {
        val response = qrRepository.getUserQrCode(serialNumber)

        return if (response is CashbackQrCodeVal) {


            if (response.value.isNotEmpty()) {

                if (response.value[0].quantity>0.0){
                    response.value[0]
                } else{
                    errorMessage = " $serialNumber уже отсканирован другим пользователем!"
                    null
                }

            } else {
                errorMessage = " $serialNumber не найден!"
                null
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun postUserQrCode(qrCodeList: ArrayList<CashbackQrCode>): QrCodeResponse? {
        val response =
            qrRepository.postUserQrCode(Mappers.mapCashbackQrCodeToRequestQrCode(qrCodeList))

        return if (response is QrCodeResponse) {
            if (response.CASHBACK_TRANS_ROWCollection.isNotEmpty()) {
                response
            } else {
                errorMessage = "ERROR"
                null
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

}
