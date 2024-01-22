package com.example.lorettocashback.presentation.login

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.domain.interactor.LoginInteractor
import com.example.lorettocashback.domain.interactor.LoginInteractorImpl
import kotlinx.coroutines.launch
import com.example.lorettocashback.core.BaseViewModel

class LoginViewModel : BaseViewModel() {
    private val interactor: LoginInteractor by lazy { LoginInteractorImpl() }

    var logged: MutableLiveData<Boolean> = MutableLiveData()
    var loginerror: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()


    fun requestLogin() {
        vmScope.launch {
            loading.postValue(true)

            val isLogged = interactor.requestLogin()
            if (isLogged) {
                logged.postValue(true)
            } else {
                loginerror.postValue(interactor.errorMessage)
            }

            loading.postValue(false)

        }
    }
}