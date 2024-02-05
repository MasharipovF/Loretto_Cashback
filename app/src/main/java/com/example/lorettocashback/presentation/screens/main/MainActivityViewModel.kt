package com.example.lorettocashback.presentation.screens.main

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel

class MainActivityViewModel : BaseViewModel() {
    var exitBtn: MutableLiveData<Unit> = MutableLiveData()
    var openNotificationBtn: MutableLiveData<Unit> = MutableLiveData()
    var openQRBtn: MutableLiveData<Unit> = MutableLiveData()
    var openHistoryBtn: MutableLiveData<Unit> = MutableLiveData()

    fun exitFun() {
        exitBtn.value = Unit
    }
    fun openNotificationFun() {
        openNotificationBtn.value = Unit
    }
    fun openQRFun() {
        openQRBtn.value = Unit
    }
    fun openHistoryFun() {
        openHistoryBtn.value = Unit
    }
}