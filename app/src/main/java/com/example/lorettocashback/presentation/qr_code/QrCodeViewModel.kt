package com.example.lorettocashback.presentation.qr_code

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.domain.interactor.QrCodeInteractor
import com.example.lorettocashback.domain.interactor.QrCodeInteractorImpl
import kotlinx.coroutines.launch

class QrCodeViewModel : BaseViewModel() {

    private val qrInteractor: QrCodeInteractor by lazy { QrCodeInteractorImpl() }

    var data: MutableLiveData<CashbackQrCode> = MutableLiveData()
    var listData: MutableLiveData<ArrayList<CashbackQrCode>> = MutableLiveData()

    fun getData(serialNumber: String) {
        vmScope.launch {
            data.postValue(qrInteractor.getUserQrCode(serialNumber))
        }
    }

    fun getDataList(data: CashbackQrCode) {

        listData.value?.add(data)
        listData.value = listData.value
    }

}