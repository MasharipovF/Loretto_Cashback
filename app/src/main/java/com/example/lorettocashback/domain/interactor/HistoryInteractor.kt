package com.example.lorettocashback.domain.interactor

import com.example.lorettocashback.data.entity.history.CashbackHistory
import com.example.lorettocashback.data.entity.history.CashbackHistoryVal
import com.example.lorettocashback.data.repository.HistoryRepository
import com.example.lorettocashback.data.repository.HistoryRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse

interface HistoryInteractor {
    suspend fun getUserHistoryAll(): List<CashbackHistory>?
    suspend fun getUserHistory(
        dateGe: String,
        dateLe: String
    ): List<CashbackHistory>?

    var errorMessage: String?
}

class HistoryInteractorImpl() : HistoryInteractor {

    private val hsRepository: HistoryRepository by lazy { HistoryRepositoryImpl() }

    override var errorMessage: String? = null
    override suspend fun getUserHistoryAll(): List<CashbackHistory> {

        val response = hsRepository.getUserHistoryAll()

        return if (response is CashbackHistoryVal) {

            if (!response.value.isNullOrEmpty()) {
                response.value
            } else {
                errorMessage = "EMPTY"
                emptyList()
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            emptyList()
        }

    }

    override suspend fun getUserHistory(
        dateGe: String,
        dateLe: String
    ): List<CashbackHistory> {

        val response =
            hsRepository.getUserHistory(dateGe = dateGe, dateLe = dateLe)

        return if (response is CashbackHistoryVal) {

            if (!response.value.isNullOrEmpty()) {
                response.value
            } else {
                errorMessage = "EMPTY"
                emptyList()
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            emptyList()
        }

    }
}