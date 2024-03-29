package com.example.lorettocashback.presentation.screens.qr_code

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.domain.interactor.QrCodeInteractor
import com.example.lorettocashback.domain.interactor.QrCodeInteractorImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class QrCodeViewModel : BaseViewModel() {

    private val qrInteractor: QrCodeInteractor by lazy { QrCodeInteractorImpl() }

    var data: MutableLiveData<CashbackQrCode> = MutableLiveData()
    var listData: MutableLiveData<List<CashbackQrCode>> = MutableLiveData()
    var resonseData: MutableLiveData<String> = MutableLiveData()
    var errorData: MutableLiveData<String> = MutableLiveData()

    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    init {
        listData.value = ArrayList()
    }

    fun getData(serialNumber: String) {
        var logic = true
        vmScope.launch {
            loading.postValue(true)

            for (list in listData.value!!) {
                if (list.serialNumber == serialNumber) {
                    logic = false
                }
            }
            if (logic) {
                val mGetUserQrCode = qrInteractor.getUserQrCode(serialNumber)

                if (mGetUserQrCode != null) {
                    data.postValue(mGetUserQrCode)
                } else {
                    errorData.postValue(qrInteractor.errorMessage)
                }
            }
            loading.postValue(false)
        }
    }

    fun getDataList(list: List<CashbackQrCode>) {
        vmScope.launch {
            listData.postValue(list)
        }
    }

    fun postQrCode(qrCodeList: ArrayList<CashbackQrCode>) {
        vmScope.launch {
            loadingButton.postValue(true)


            val mQrCodeList = qrInteractor.postUserQrCode(qrCodeList)

            if (mQrCodeList != null) {
                resonseData.postValue("SUCCESFUL")
            } else {
                errorData.postValue(qrInteractor.errorMessage)
            }

            loadingButton.postValue(false)
        }
    }

}