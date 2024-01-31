package com.example.lorettocashback.data.repository

import com.example.lorettocashback.data.remote.services.QrCodeService
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.retryIO

interface QrCodeRepository {
    suspend fun getUserQrCode(
        serialNumber: String
    ): Any?

}

class QrCodeRepositoryImpl(
    private val qrService: QrCodeService = QrCodeService.get(),
) : QrCodeRepository {
    override suspend fun getUserQrCode(serialNumber: String): Any? {
        val response = retryIO {
            val filterString =
                "SerialNumber eq '$serialNumber' or Asllik eq '$serialNumber'"

            qrService.getUserQrCodeData(
                filter = filterString
            )
        }

        return if (response.isSuccessful) {
            response.body()
        } else {
            ErrorUtils.errorProcess(response)
        }
    }

}