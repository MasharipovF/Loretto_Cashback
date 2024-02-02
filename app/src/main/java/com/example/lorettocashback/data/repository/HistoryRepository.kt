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


    suspend fun getTotalSum(logic: Boolean): Any?
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

    override suspend fun getTotalSum(logic: Boolean): Any? {
        val response = retryIO {
            val applyGroupBy = "groupby((OperationType))"
            val applyAggregate = "aggregate(CashbackAmount with sum as CashbackAmount )"
            val filter = "OperationType eq 'WITHDREW'"

            if (logic) {
                hsService.getTotalSum(
                    applyGroupBy = applyGroupBy,
                    applyAggregate = applyAggregate,
                    filter = filter
                )
            } else {
                hsService.getTotalSum(
                    applyGroupBy = null,
                    applyAggregate = applyAggregate,
                    filter = null
                )
            }
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            ErrorUtils.errorProcess(response)
        }
    }
}