package com.example.lorettocashback.presentation.main

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.masterdatas.AppVersion
import com.example.lorettocashback.data.entity.masterdatas.CompanyInfo
import com.example.lorettocashback.domain.interactor.*
import com.example.lorettocashback.util.enums.ActivityBpTypes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainActivityViewModel : BaseViewModel() {

    private val masterDataInteractor: MasterDataInteractor by lazy { MasterDataInteractorImpl() }

    var exchangeRate: MutableLiveData<Double> = MutableLiveData()

    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var errorLoading: MutableLiveData<Boolean> = MutableLiveData()

    var companyInfo: MutableLiveData<CompanyInfo> = MutableLiveData()

    var appVersion: MutableLiveData<AppVersion> = MutableLiveData()


    init {
        getCompanyInfo()
    }

    fun getAppVersion() {
       /* Log.wtf("APP_VERSION", "LOADING")
        vmScope.launch {
            val version = masterDataInteractor.getAppVersion()
            if (version != null) {
                appVersion.postValue(version)
                Log.wtf("APP_VERSION", version.toString())
            }
            Log.wtf("APP_VERSION", version.toString())

        }*/
    }


    /*fun getExchangeRate() {
        vmScope.launch {
            loading.postValue(true)

            val response = masterDataInteractor.getExchangeRate()
            if (response != null) {
                exchangeRate.postValue(response)
            } else
                errorItem.postValue("Ошибка при загрузке: ${masterDataInteractor.errorMessage}")

            loading.postValue(false)
        }
    }*/

    fun getCompanyInfo() {
        vmScope.launch {

            val response = masterDataInteractor.getCompanyInfo()
            if (response != null) {
                Preferences.totalsAccuracy = response.totalsAccuracy
                Preferences.pricesAccuracy = response.priceAccuracy
            } else {
                errorItem.postValue("Ошибка при загрузке данных компании: ${masterDataInteractor.errorMessage}")
            }
        }
    }

}