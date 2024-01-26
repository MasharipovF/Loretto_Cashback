package com.example.lorettocashback.presentation.history

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel

class HistoryViewModel : BaseViewModel() {
    var textDate1: MutableLiveData<String> = MutableLiveData()
    var textDate2: MutableLiveData<String> = MutableLiveData()

    fun textDateFun1(text: String) {
        textDate1.value = text
    }
    fun textDateFun2(text: String) {
        textDate2.value = text
    }

}