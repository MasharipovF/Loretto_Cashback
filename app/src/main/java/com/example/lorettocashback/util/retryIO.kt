package com.example.lorettocashback.util

import android.util.Log
import kotlinx.coroutines.delay
import java.io.IOException

suspend fun <T> retryIO(
    times: Int = 3,
    initialDelay: Long = 500, // 0.5 second
    maxDelay: Long = 1000,    // 100 second
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    Log.wtf("RETRY", "SEDF")
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            Log.d("EXCEPTION_RETRY", e.message.toString())
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}