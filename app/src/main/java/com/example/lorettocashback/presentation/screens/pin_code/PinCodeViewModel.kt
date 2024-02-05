package com.example.lorettocashback.presentation.screens.pin_code

import androidx.lifecycle.MutableLiveData
import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.businesspartners.CashbackUsers
import com.example.lorettocashback.domain.interactor.LoginInteractor
import com.example.lorettocashback.domain.interactor.LoginInteractorImpl
import kotlinx.coroutines.launch

class PinCodeViewModel : BaseViewModel() {
    private val interactor: LoginInteractor by lazy { LoginInteractorImpl() }

    var loggedUser: MutableLiveData<CashbackUsers> = MutableLiveData()
    var loginError: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()


    fun requestLogin(pinCode: String) {

        vmScope.launch {
            loading.postValue(true)

            if (pinCode == Preferences.pinCode) {
                val mLoggedUser =
                    interactor.requestLogin(Preferences.phoneNumber, Preferences.password)

                if (mLoggedUser != null) {
                    loggedUser.postValue(mLoggedUser)
                } else {
                    loginError.postValue(interactor.errorMessage)
                }

            }else{
                loginError.postValue("Wrong pincode")
            }

            loading.postValue(false)

        }
    }
}