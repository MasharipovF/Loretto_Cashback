package com.example.lorettocashback.presentation.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.domain.interactor.LoginInteractor
import com.example.lorettocashback.domain.interactor.LoginInteractorImpl
import kotlinx.coroutines.launch
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.entity.businesspartners.BusinessPartners

class LoginViewModel : BaseViewModel() {
    private val interactor: LoginInteractor by lazy { LoginInteractorImpl() }

    var loggedUser: MutableLiveData<BusinessPartners> = MutableLiveData()
    var loginerror: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()


    fun requestLogin(login: String, password: String) {

        vmScope.launch {
            loading.postValue(true)

            val mLoggedUser = interactor.requestLogin(login, password)


            if (mLoggedUser != null) {
                loggedUser.postValue(mLoggedUser)
            } else {
                loginerror.postValue(interactor.errorMessage)
            }

            loading.postValue(false)

        }
    }
}