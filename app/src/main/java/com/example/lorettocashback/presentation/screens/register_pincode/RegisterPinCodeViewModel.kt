package com.example.lorettocashback.presentation.screens.register_pincode

import com.example.lorettocashback.core.BaseViewModel
import com.example.lorettocashback.data.Preferences

class RegisterPinCodeViewModel : BaseViewModel() {
    fun savePinCode(code: String) {
        Preferences.pinCode = code
    }
}