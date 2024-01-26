package com.example.lorettocashback.presentation.main

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.businesspartners.BusinessPartners
import com.example.lorettocashback.data.entity.masterdatas.AppVersion
import com.example.lorettocashback.data.entity.masterdatas.CompanyInfo
import com.example.lorettocashback.domain.interactor.*
import com.example.lorettocashback.util.enums.ActivityBpTypes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

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