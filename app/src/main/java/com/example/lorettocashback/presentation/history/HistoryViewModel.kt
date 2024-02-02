package com.example.lorettocashback.presentation.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.model.StatusEnum
import com.example.lorettocashback.domain.interactor.HistoryInteractor
import com.example.lorettocashback.domain.interactor.HistoryInteractorImpl
import com.example.lorettocashback.util.LoadMore
import kotlinx.coroutines.launch

class HistoryViewModel : BaseViewModel() {

    private val hsInteractor: HistoryInteractor by lazy { HistoryInteractorImpl() }

    var listData: MutableLiveData<List<Any>> = MutableLiveData()
    var dateFrom: MutableLiveData<String> = MutableLiveData()
    var dateTo: MutableLiveData<String> = MutableLiveData()
    var errorData: MutableLiveData<String> = MutableLiveData()
    var errorDataSum: MutableLiveData<String> = MutableLiveData()
    var cashbackDataWithdrew: MutableLiveData<Double> = MutableLiveData()
    var cashbackDataGain: MutableLiveData<Double> = MutableLiveData()

    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {

    }

    fun textDateDeFun(text: String) {
        dateFrom.value = text
    }

    fun textDateLeFun(text: String) {
        dateTo.value = text
    }


    fun listDateQuery(skip: Int = 0) {
        cashback()

        vmScope.launch {
            if (skip == 0) {
                loading.postValue(true)
            }

            val listResult = if (skip == 0) arrayListOf() else {
                ArrayList<Any>(listData.value?.filter { it !is LoadMore } ?: listOf())
            }

            val mGetUserHistory = hsInteractor.getUserHistory(
                dateGe = dateFrom.value,
                dateLe = dateTo.value,
                skip
            )

            Log.d("HISTORY", mGetUserHistory.toString())

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
            val mCashbackAll = hsInteractor.getTotalSum(
                dateFrom = dateFrom.value,
                dateTo = dateTo.value
            )

            if (mCashbackAll != null) {

                var gained = 0.0
                var withdraw = 0.0

                mCashbackAll.forEach {
                    if (it.operationType == StatusEnum.WITHDRAW.statusName) {
                        withdraw += it.cashbackAmount
                    } else {
                        gained += it.cashbackAmount
                    }
                }
                cashbackDataWithdrew.postValue(withdraw)
                cashbackDataGain.postValue(gained)

            } else {
                errorDataSum.postValue(hsInteractor.errorMessageSum)
            }

        }
    }
}