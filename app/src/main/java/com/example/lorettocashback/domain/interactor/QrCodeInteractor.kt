package com.example.lorettocashback.domain.interactor

import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.history.CashbackHistoryVal
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCodeVal
import com.example.lorettocashback.data.repository.HistoryRepository
import com.example.lorettocashback.data.repository.HistoryRepositoryImpl
import com.example.lorettocashback.data.repository.QrCodeRepository
import com.example.lorettocashback.data.repository.QrCodeRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse

interface QrCodeInteractor {
    suspend fun getUserQrCode(
        serialNumber: String
    ): CashbackQrCode?

    var errorMessage: String?
}

class QrCodeInteractorImpl() : QrCodeInteractor {

    private val qrRepository: QrCodeRepository by lazy { QrCodeRepositoryImpl() }

    override var errorMessage: String? = null
    override suspend fun getUserQrCode(serialNumber: String): CashbackQrCode? {
        val response = qrRepository.getUserQrCode(serialNumber)

        return if (response is CashbackQrCodeVal) {

            if (!response.value.isNullOrEmpty()) {
                response.value[0]
            } else {
                errorMessage = " $serialNumber не найден!"
                null
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

}
