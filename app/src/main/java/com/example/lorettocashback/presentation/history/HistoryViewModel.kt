package com.example.lorettocashback.presentation.history

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel

class HistoryViewModel : BaseViewModel() {
    var textDate1: MutableLiveData<String> = MutableLiveData()
    var textDate2: MutableLiveData<String> = MutableLiveData()
    var clickCancel: MutableLiveData<Unit> = MutableLiveData()

    fun textDate1Fun(text: String) {
        textDate1.value = text
    }

    fun textDate2Fun(text: String) {
        textDate2.value = text
    }

    fun clickCancelFun() {
        clickCancel.value = Unit
    }

}