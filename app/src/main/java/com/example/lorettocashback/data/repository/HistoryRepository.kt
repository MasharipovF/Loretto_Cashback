package com.example.lorettocashback.data.repository

import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.remote.services.HistoryService
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.retryIO

interface HistoryRepository {

    suspend fun getUserHistoryAll(): Any?

    suspend fun getUserHistory(
        dateGe: String,
        dateLe: String
    ): Any?

}
class HistoryRepositoryImpl(
    private val hsService: HistoryService = HistoryService.get(),
) : HistoryRepository {

    private val userCode = Preferences.userCode
    override suspend fun getUserHistoryAll(): Any? {
        val response = retryIO {
            val filterString =
                "UserCode  eq '$userCode'"

            hsService.getUserHistoryData(
                filter = filterString
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getUserHistory(dateGe: String, dateLe: String): Any? {
        val response = retryIO {
            val filterString =
                "UserCode  eq '$userCode'  and Date ge '$dateGe' and Date le '$dateLe'"

            hsService.getUserHistoryData(
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