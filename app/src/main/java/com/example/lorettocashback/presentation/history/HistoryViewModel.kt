package com.example.lorettocashback.presentation.history

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.entity.history.CashbackHistory
import com.example.lorettocashback.domain.interactor.HistoryInteractor
import com.example.lorettocashback.domain.interactor.HistoryInteractorImpl
import kotlinx.coroutines.launch

class HistoryViewModel : BaseViewModel() {

    private val hsInteractor: HistoryInteractor by lazy { HistoryInteractorImpl() }

    var listDataAll: MutableLiveData<List<CashbackHistory>> = MutableLiveData()
    var listDataQuery: MutableLiveData<List<CashbackHistory>> = MutableLiveData()
    var textDate1: MutableLiveData<String> = MutableLiveData()
    var textDate2: MutableLiveData<String> = MutableLiveData()
    var clickCancel: MutableLiveData<Unit> = MutableLiveData()
    var loginError: MutableLiveData<String> = MutableLiveData()

    var loading: MutableLiveData<Boolean> = MutableLiveData()


    fun textDate1Fun(text: String) {
        textDate1.value = text
    }

    fun textDate2Fun(text: String) {
        textDate2.value = text
    }

    fun clickCancelFun() {
        clickCancel.value = Unit
    }

    fun listDateAllFun() {

        vmScope.launch {
            loading.postValue(true)

            val mGetUserHistory = hsInteractor.getUserHistoryAll()

            if (mGetUserHistory != null) {
                listDataAll.postValue(hsInteractor.getUserHistoryAll())
            } else {
                loginError.postValue(hsInteractor.errorMessage)
            }

            loading.postValue(false)
        }

    }

    fun listDateQuery(dateGe: String, dateLe: String) {
        vmScope.launch {
            loading.postValue(true)

            val mGetUserHistory = hsInteractor.getUserHistory(dateGe = dateGe, dateLe = dateLe)

            if (mGetUserHistory != null) {
                listDataAll.postValue(hsInteractor.getUserHistory(dateGe = dateGe, dateLe = dateLe))
            } else {
                loginError.postValue(hsInteractor.errorMessage)
            }

            loading.postValue(false)
        }
    }

//    fun getItems(skipValue: Int = 0) {
//        if (itemSearchJob?.isActive == true) {
//            itemSearchJob?.cancel()
//        }
//
//        itemSearchJob = vmScope.launch {
//            if (skipValue == 0) {
//                loading.postValue(true)
//            }
//
//            val listResult = if (skipValue==0) arrayListOf() else {
//                ArrayList<Any>(listToDraw.value?.filter { it !is LoadMore } ?: listOf())
//            }
//
//            val items = interactor.getItemsViaSML(
//                filter = filterString.value?:GeneralConsts.EMPTY_STRING,
//                whsCode = Preferences.defaultWhs?:GeneralConsts.EMPTY_STRING,
//                cardCode = GeneralConsts.EMPTY_STRING,
//                date=Utils.getCurrentDateinUSAFormat(),
//                priceListCode = Preferences.defaultPriceList,
//                onlyOnHand = isOnlyOnHandVisible.value ?: false,
//                itemGroupCode = currentChosenItemGroup.value?.GroupCode,
//                isGeneralItemsList = true,
//                skipValue = skipValue
//            )
//            if (items != null) {
//
//                listResult.addAll(items)
//
//                if (items.size >= GeneralConsts.MAX_PAGE_SIZE) listResult.add(LoadMore())
//            } else {
//                errorLoading.postValue(true)
//            }
//            loading.postValue(false)
//            listToDraw.postValue(listResult)
//        }
//    }


}