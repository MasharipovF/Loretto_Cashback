package com.example.lorettocashback.presentation

import android.app.Application
import android.content.Context
import com.example.lorettocashback.data.Preferences

class SapMobileApplication: Application() {

    companion object{
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
        context = this
    }

}