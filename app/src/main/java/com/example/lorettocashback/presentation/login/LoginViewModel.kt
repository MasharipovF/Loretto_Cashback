package com.example.lorettocashback.presentation.login

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.domain.interactor.LoginInteractor
import com.example.lorettocashback.domain.interactor.LoginInteractorImpl
import kotlinx.coroutines.launch
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.entity.businesspartners.CashbackUsers

class LoginViewModel : BaseViewModel() {
    private val interactor: LoginInteractor by lazy { LoginInteractorImpl() }

    var loggedUser: MutableLiveData<CashbackUsers> = MutableLiveData()
    var loginerror: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()


    fun requestLogin(phone: String, password: String) {

        vmScope.launch {
            loading.postValue(true)

            val mLoggedUser = interactor.requestLogin(phone, password)

            if (mLoggedUser != null) {
                loggedUser.postValue(mLoggedUser)
            } else {
                loginerror.postValue(interactor.errorMessage)
            }

            loading.postValue(false)

        }
    }
}