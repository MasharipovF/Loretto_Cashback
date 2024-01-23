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
            Log.d("TTTU", "$mLoggedUser")


            if (loggedUser != null) {
                Log.d("TTTU", "requestLogin: kirdi")
                loggedUser.value = mLoggedUser
            } else {
                Log.d("TTTU", "requestLogin: tash")

                loginerror.value = interactor.errorMessage
            }

            loading.postValue(false)

        }
    }
}