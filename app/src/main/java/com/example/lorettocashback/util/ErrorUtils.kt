package com.example.lorettocashback.util

import android.util.Log
import com.example.lorettocashback.domain.dto.error.Error
import com.example.lorettocashback.domain.dto.error.ErrorResponse
import com.example.lorettocashback.domain.dto.error.ErrorResponseV2
import com.example.lorettocashback.domain.dto.error.Message
import com.google.gson.Gson
import retrofit2.Response

abstract class ErrorUtils {
    companion object {
        fun <T> errorProcess(responseBody: Response<T>): ErrorResponse {
            //
            // Log.d("ERROR_BODY", responseBody.errorBody()!!.string())
            val body = responseBody.errorBody()!!.string()
            var error:ErrorResponse? = null
            var errorV2: ErrorResponseV2? = null
            var errorMessage = "Неизвестная ошибка"
            var errorCode = -1

            try {
                error= Gson().fromJson(body, ErrorResponse::class.java)
                errorMessage = error.error.message.value.toString()
                errorCode = error.error.code
            } catch (e: Exception){
                errorV2 = Gson().fromJson(body, ErrorResponseV2::class.java)
                errorMessage = errorV2?.error?.message?:responseBody.errorBody()!!.string()
                errorCode = errorV2?.error?.code?.toInt()?:1
                //Log.d("ERROR_BODY", errorMessage)
            }

            //Log.d("ERROR_BODY", errorMessage)
            error = ErrorResponse(
                Error(
                    errorCode,
                    Message("ru", errorMessage)
                )
            )

            Log.d("ERROR_BODY", error.toString())

            return error
        }
    }
}