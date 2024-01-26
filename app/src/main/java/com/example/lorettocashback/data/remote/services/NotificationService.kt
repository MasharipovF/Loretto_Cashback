package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder

interface NotificationService {
    companion object {

        fun get(): NotificationService =
            ServiceBuilder.createService(NotificationService::class.java)
    }


}