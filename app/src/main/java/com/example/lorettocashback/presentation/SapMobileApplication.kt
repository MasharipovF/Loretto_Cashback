package com.example.lorettocashback.presentation

import android.app.Application
import com.example.lorettocashback.data.Preferences

class SapMobileApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
    }

}