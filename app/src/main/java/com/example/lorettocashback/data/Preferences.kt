package com.example.lorettocashback.data

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    const val CREDENTIALS = "CREDENTIALS"


    fun init(context: Context) {
        preferences = context.getSharedPreferences(CREDENTIALS, Context.MODE_PRIVATE)
    }

    fun setPreference(preferences: SharedPreferences) {
        Preferences.preferences = preferences
    }

    lateinit var preferences: SharedPreferences


    var noPaginationConfigutation: Boolean
        get() = preferences.getBoolean(Preferences::noPaginationConfigutation.name, false)
        set(value) {
            preferences.edit().putBoolean(Preferences::noPaginationConfigutation.name, value).apply()
        }

    var companyDB: String?
        get() = preferences.getString(Preferences::companyDB.name, null)
        set(value) {
            preferences.edit().putString(Preferences::companyDB.name, value).apply()
        }

    var ipAddress: String?
        get() = preferences.getString(Preferences::ipAddress.name, null)
        set(value) {
            preferences.edit().putString(Preferences::ipAddress.name, value).apply()
        }

    var ISCONNECT: Boolean
        get() = preferences.getBoolean(Preferences::ISCONNECT.name, false)
        set(value) {
            preferences.edit().putBoolean(Preferences::ISCONNECT.name, value).apply()
        }


    var portNumber: String?
        get() = preferences.getString(Preferences::portNumber.name, null)
        set(value) {
            preferences.edit().putString(Preferences::portNumber.name, value).apply()
        }

    var sessionID: String?
        get() = preferences.getString(Preferences::sessionID.name, null)
        set(value) {
            preferences.edit().putString(Preferences::sessionID.name, value).apply()
        }

    var firstLogin: Boolean
        get() = preferences.getBoolean(Preferences::firstLogin.name, true)
        set(value) {
            preferences.edit().putBoolean(Preferences::firstLogin.name, value).apply()
        }


    var cashbackToken: String?
        get() = preferences.getString(Preferences::cashbackToken.name, null)
        set(value) {
            preferences.edit().putString(Preferences::cashbackToken.name, value).apply()
        }

    var userName: String?
        get() = preferences.getString(Preferences::userName.name, null)
        set(value) {
            preferences.edit().putString(Preferences::userName.name, value).apply()
        }


    var userPassword: String?
        get() = preferences.getString(Preferences::userPassword.name, null)
        set(value) {
            preferences.edit().putString(Preferences::userPassword.name, value).apply()
        }

    var isSuperUser: Boolean
        get() = preferences.getBoolean(Preferences::isSuperUser.name, false)
        set(value) {
            preferences.edit().putBoolean(Preferences::isSuperUser.name, value).apply()
        }

    var defaultWhs: String?
        get() = preferences.getString(Preferences::defaultWhs.name, null)
        set(value) {
            preferences.edit().putString(Preferences::defaultWhs.name, value).apply()
        }

    var cardName: String?
        get() = preferences.getString(Preferences::cardName.name, null)
        set(value) {
            preferences.edit().putString(Preferences::cardName.name, value).apply()
        }

    var returnsPassword: String?
        get() = preferences.getString(Preferences::returnsPassword.name, null)
        set(value) {
            preferences.edit().putString(Preferences::returnsPassword.name, value).apply()
        }

    var intermediaryWhs: String?
        get() = preferences.getString(Preferences::intermediaryWhs.name, null)
        set(value) {
            preferences.edit().putString(Preferences::intermediaryWhs.name, value).apply()
        }

    var defectWhs: String?
        get() = preferences.getString(Preferences::defectWhs.name, null)
        set(value) {
            preferences.edit().putString(Preferences::defectWhs.name, value).apply()
        }

    var salesOrdersVisibleDays: Int
        get() = preferences.getInt(Preferences::salesOrdersVisibleDays.name, 10000)
        set(value) {
            preferences.edit().putInt(Preferences::salesOrdersVisibleDays.name, value).apply()
        }

    var salesOrdersChangeLimit: Int
        get() = preferences.getInt(Preferences::salesOrdersChangeLimit.name, 10000)
        set(value) {
            preferences.edit().putInt(Preferences::salesOrdersChangeLimit.name, value).apply()
        }

    var cashbackItemCode: String?
        get() = preferences.getString(Preferences::cashbackItemCode.name, null)
        set(value) {
            preferences.edit().putString(Preferences::cashbackItemCode.name, value).apply()
        }

    var restrictDebtAfter: Int
        get() = preferences.getInt(Preferences::restrictDebtAfter.name, -1)
        set(value) {
            preferences.edit().putInt(Preferences::restrictDebtAfter.name, value).apply()
        }

    var defaultCustomer: String?
        get() = preferences.getString(Preferences::defaultCustomer.name, null)
        set(value) {
            preferences.edit().putString(Preferences::defaultCustomer.name, value).apply()
        }

    var branch: String?
        get() = preferences.getString(Preferences::branch.name, null)
        set(value) {
            preferences.edit().putString(Preferences::branch.name, value).apply()
        }

    var accountUSD: String?
        get() = preferences.getString(Preferences::accountUSD.name, null)
        set(value) {
            preferences.edit().putString(Preferences::accountUSD.name, value).apply()
        }

    var accountUZS: String?
        get() = preferences.getString(Preferences::accountUZS.name, null)
        set(value) {
            preferences.edit().putString(Preferences::accountUZS.name, value).apply()
        }

    var currencyRate: Double
        get() = java.lang.Double.longBitsToDouble(
            preferences.getLong(
                Preferences::currencyRate.name,
                0L
            )
        )
        set(value) {
            preferences.edit().putLong(
                Preferences::currencyRate.name,
                java.lang.Double.doubleToRawLongBits(value)
            ).apply()
        }

    var currencyDate: String?
        get() = preferences.getString(Preferences::currencyDate.name, null)
        set(value) {
            preferences.edit().putString(Preferences::currencyDate.name, value).apply()
        }

    var totalsAccuracy: Int
        get() = preferences.getInt(Preferences::totalsAccuracy.name, 6)
        set(value) {
            preferences.edit().putInt(Preferences::totalsAccuracy.name, value).apply()
        }

    var pricesAccuracy: Int
        get() = preferences.getInt(Preferences::pricesAccuracy.name, 6)
        set(value) {
            preferences.edit().putInt(Preferences::pricesAccuracy.name, value).apply()
        }


    //PRINTER SETTINGS

    var printerIp: String?
        get() = preferences.getString(Preferences::printerIp.name, null)
        set(value) {
            preferences.edit().putString(Preferences::printerIp.name, value).apply()
        }

    var printerPort: Int
        get() = preferences.getInt(Preferences::printerPort.name, 0)
        set(value) {
            preferences.edit().putInt(Preferences::printerPort.name, value).apply()
        }

    var printerDPI: Int
        get() = preferences.getInt(Preferences::printerDPI.name, 0)
        set(value) {
            preferences.edit().putInt(Preferences::printerDPI.name, value).apply()
        }

    var printerMaxChar: Int
        get() = preferences.getInt(Preferences::printerMaxChar.name, 0)
        set(value) {
            preferences.edit().putInt(Preferences::printerMaxChar.name, value).apply()
        }

    var printerWidth: Int
        get() = preferences.getInt(Preferences::printerWidth.name, 0)
        set(value) {
            preferences.edit().putInt(Preferences::printerWidth.name, value).apply()
        }

    var receiptHeaderText: String?
        get() = preferences.getString(Preferences::receiptHeaderText.name, null)
        set(value) {
            preferences.edit().putString(Preferences::receiptHeaderText.name, value).apply()
        }

    var receiptShopText: String?
        get() = preferences.getString(Preferences::receiptShopText.name, null)
        set(value) {
            preferences.edit().putString(Preferences::receiptShopText.name, value).apply()
        }

    var receiptBottomText: String?
        get() = preferences.getString(Preferences::receiptBottomText.name, null)
        set(value) {
            preferences.edit().putString(Preferences::receiptBottomText.name, value).apply()
        }

    var discountByDocTotal: String?
        get() = preferences.getString(Preferences::discountByDocTotal.name, null)
        set(value) {
            preferences.edit().putString(Preferences::discountByDocTotal.name, value).apply()
        }



}