package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder

interface HistoryService {
    companion object {

        fun get(): HistoryService =
            ServiceBuilder.createService(HistoryService::class.java)
    }


}