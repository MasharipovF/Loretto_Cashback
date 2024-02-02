package com.example.lorettocashback.domain.interactor

import com.example.lorettocashback.data.entity.history.CashbackAmount
import com.example.lorettocashback.data.entity.history.CashbackAmountVal
import com.example.lorettocashback.data.entity.history.CashbackHistory
import com.example.lorettocashback.data.entity.history.CashbackHistoryVal
import com.example.lorettocashback.data.repository.HistoryRepository
import com.example.lorettocashback.data.repository.HistoryRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse

interface HistoryInteractor {
    suspend fun getUserHistory(
        dateGe: String?,
        dateLe: String?,
        skip: Int?
    ): List<CashbackHistory>?

    suspend fun getTotalSum(
        dateFrom: String?,
        dateTo: String?
    ): List<CashbackAmount>?


    var errorMessage: String?
    var errorMessageSum: String?
}

class HistoryInteractorImpl() : HistoryInteractor {

    private val hsRepository: HistoryRepository by lazy { HistoryRepositoryImpl() }

    override var errorMessage: String? = null
    override var errorMessageSum: String? = null
    override suspend fun getUserHistory(
        dateGe: String?,
        dateLe: String?,
        skip: Int?
    ): List<CashbackHistory> {

        val response = hsRepository.getUserHistory(dateGe = dateGe, dateLe = dateLe, skip)

        return if (response is CashbackHistoryVal) {
            if (response.value.isNotEmpty()) {
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

    override suspend fun getTotalSum(
        dateFrom: String?,
        dateTo: String?
    ): List<CashbackAmount>? {
        val response = hsRepository.getTotalSum(dateFrom, dateTo)

        return if (response is CashbackAmountVal) {
            if (response.value.isNotEmpty()) {
                response.value
            } else {
                errorMessageSum = "EMPTY"
                null
            }

        } else {
            errorMessageSum = (response as ErrorResponse).error.message.value
            null
        }
    }
}