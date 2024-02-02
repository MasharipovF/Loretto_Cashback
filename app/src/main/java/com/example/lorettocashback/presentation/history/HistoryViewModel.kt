package com.example.lorettocashback.presentation.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.entity.history.CashbackAmount
import com.example.lorettocashback.domain.interactor.HistoryInteractor
import com.example.lorettocashback.domain.interactor.HistoryInteractorImpl
import com.example.lorettocashback.util.LoadMore
import kotlinx.coroutines.launch

class HistoryViewModel : BaseViewModel() {

    private val hsInteractor: HistoryInteractor by lazy { HistoryInteractorImpl() }

    var listData: MutableLiveData<List<Any>> = MutableLiveData()
    var textDateDe: MutableLiveData<String> = MutableLiveData()
    var textDateLe: MutableLiveData<String> = MutableLiveData()
    var clickCancel: MutableLiveData<Unit> = MutableLiveData()
    var errorData: MutableLiveData<String> = MutableLiveData()
    var errorDataSum: MutableLiveData<String> = MutableLiveData()
    var cashbackDataWithdrew: MutableLiveData<CashbackAmount> = MutableLiveData()
    var cashbackDataAll: MutableLiveData<CashbackAmount> = MutableLiveData()

    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        cashback()
    }

    fun textDateDeFun(text: String) {
        textDateDe.value = text
    }

    fun textDateLeFun(text: String) {
        textDateLe.value = text
    }

    fun clickCancelFun() {
        clickCancel.value = Unit
    }

    fun listDateQuery(skip: Int = 0, dateDe: String? = null, dateLe: String? = null) {
        vmScope.launch {
            if (skip == 0) {
                loading.postValue(true)
            }

            val listResult = if (skip == 0) arrayListOf() else {
                ArrayList<Any>(listData.value?.filter { it !is LoadMore } ?: listOf())
            }

            val mGetUserHistory = hsInteractor.getUserHistory(
                dateGe = dateDe,
                dateLe = dateLe,
                skip
            )

            if (mGetUserHistory != null) {

                listResult.addAll(mGetUserHistory)

                if (mGetUserHistory.size >= GeneralConsts.MAX_PAGE_SIZE) listResult.add(LoadMore())

                listData.postValue(listResult)

            } else {
                errorData.postValue(hsInteractor.errorMessage)
            }
            loading.postValue(false)
        }
    }

    fun cashback() {

        vmScope.launch {
            val mCashbackWithdrew = hsInteractor.getTotalSum(true)
            val mCashbackAll = hsInteractor.getTotalSum(false)

            if (mCashbackWithdrew != null) {
                cashbackDataWithdrew.postValue(mCashbackWithdrew)
            } else {
//                errorDataSum.postValue(hsInteractor.errorMessageSum)
            }

            if (mCashbackAll != null) {
                cashbackDataAll.postValue(mCashbackAll)
            } else {
                errorDataSum.postValue(hsInteractor.errorMessageSum)
            }
        }
    }
}