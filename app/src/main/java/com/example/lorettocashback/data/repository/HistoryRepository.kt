package com.example.lorettocashback.data.repository

import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.remote.services.HistoryService
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.retryIO

interface HistoryRepository {

    suspend fun getUserHistory(
        dateGe: String?,
        dateLe: String?,
        skip: Int?
    ): Any?


    suspend fun getTotalSum(
        dateFrom: String?,
        dateTo: String?
    ): Any?
}

class HistoryRepositoryImpl(
    private val hsService: HistoryService = HistoryService.get(),
) : HistoryRepository {

    private val userCode = Preferences.userCode
    override suspend fun getUserHistory(dateGe: String?, dateLe: String?, skip: Int?): Any? {
        val response = retryIO {
            var filterString = "UserCode  eq '$userCode'"

            if (dateGe != null) {
                filterString += " and Date ge '$dateGe'"
            }

            if (dateLe != null) {
                filterString += " and Date le '$dateLe'"
            }


            hsService.getUserHistoryData(
                filter = filterString,
                skipValue = skip
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getTotalSum(dateFrom: String?, dateTo: String?): Any? {
        val response = retryIO {
            val applyGroupBy = "groupby((OperationType))"
            val applyAggregate = "aggregate(CashbackAmount with sum as CashbackAmount)"

            var filterString = "UserCode  eq '$userCode'"

            if (dateFrom != null) {
                filterString += " and Date ge '$dateFrom'"
            }

            if (dateTo != null) {
                filterString += " and Date le '$dateTo'"
            }


            hsService.getTotalSum(
                applyGroupBy = applyGroupBy,
                applyAggregate = applyAggregate,
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