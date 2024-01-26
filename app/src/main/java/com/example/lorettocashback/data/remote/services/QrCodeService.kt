package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder

interface QrCodeService {
    companion object {

        fun get(): QrCodeService =
            ServiceBuilder.createService(QrCodeService::class.java)
    }
}