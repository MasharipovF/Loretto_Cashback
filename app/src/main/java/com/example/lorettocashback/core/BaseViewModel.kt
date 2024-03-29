package com.example.lorettocashback.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lorettocashback.util.LiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("EXCEPTION_RETRY_CONNECTION", exception.message.toString())

        onException(exception)
    }

    val vmScope = viewModelScope + handler + Dispatchers.IO
    val updateList: LiveEvent<Int> = LiveEvent()
    val updateItem: LiveEvent<String> = LiveEvent()
    val removeItem: LiveEvent<String> = LiveEvent()
    val insertItem: LiveEvent<String> = LiveEvent()
    val errorItem: LiveEvent<String> = LiveEvent()
    val moveItem: LiveEvent<Pair<Int, Int>> = LiveEvent()
    var errorString: String = ""

    val connectionError: LiveEvent<Boolean> = LiveEvent()

    open fun onException(ex: Throwable) {
        connectionError.call()
        errorString = ex.message.toString()
        ex.printStackTrace()
    }


}