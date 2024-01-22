package com.example.lorettocashback.data.repository

import android.util.Log
import com.example.lorettocashback.core.ErrorCodeEnums
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.masterdatas.*
import com.example.lorettocashback.data.entity.series.SeriesForPost
import com.example.lorettocashback.data.remote.services.LoginService
import com.example.lorettocashback.data.remote.services.MasterDataServices
import com.example.lorettocashback.domain.dto.login.LoginRequestDto
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.retryIO

interface MasterDataRepository {
    suspend fun getAppVersion(): AppVersionVal?
    suspend fun getWarehouses(): WarehousesVal?
    suspend fun getItemsGroups(): ItemsGroupVal?
    suspend fun getUomGroups(): UnitOfMeasurementGroupsVal?
    suspend fun getUoms(): UnitOfMeasurementVal?
    suspend fun getPriceLists(): PriceListsVal?
    suspend fun getBpGroups(bpGroupType: String?): BusinessPartnerGroupsVal?
    suspend fun getLastBarCode(): Any?
    suspend fun getExchangeRate(): Any?
    suspend fun getSalesManagers(): Any?
    suspend fun getSalesManager(managerCode: Long): Any?
    suspend fun getSeries(params: SeriesForPost): Any?
    suspend fun getDefaultSeries(params: SeriesForPost): Any?
    suspend fun getCompanyInfo(): Any?
}

class MasterDataRepositoryImpl(
    private val masterDataServices: MasterDataServices = MasterDataServices.get(),
    private val loginService: LoginService = LoginService.get()
) : MasterDataRepository {

    override suspend fun getAppVersion(): AppVersionVal? {
        return retryIO { masterDataServices.getAppVersion().body() }
    }

    override suspend fun getWarehouses(): WarehousesVal? {
        return retryIO { masterDataServices.getWarehouses().body() }
    }

    override suspend fun getItemsGroups(): ItemsGroupVal? {
        return retryIO { masterDataServices.getItemsGroup().body() }
    }

    override suspend fun getUomGroups(): UnitOfMeasurementGroupsVal? {
        return retryIO { masterDataServices.getUnitOfMeasureGroups().body() }
    }

    override suspend fun getUoms(): UnitOfMeasurementVal? {
        return retryIO { masterDataServices.getUnitOfMeasures().body() }
    }

    override suspend fun getPriceLists(): PriceListsVal? {
        return retryIO { masterDataServices.getPriceLists().body() }
    }

    override suspend fun getBpGroups(bpGroupType: String?): BusinessPartnerGroupsVal? {
        return if (bpGroupType == null) {
            Log.d("BPGROUP", "NO TYPE")
            retryIO { masterDataServices.getBpGroups().body() }
        } else {
            Log.d("BPGROUP", bpGroupType.toString())
            retryIO { masterDataServices.getBpGroups(filter = "Type eq '$bpGroupType'").body() }
        }
    }

    override suspend fun getLastBarCode(): Any? {
        val response = retryIO { masterDataServices.getLastBarCode() }
        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }

    }

    override suspend fun getExchangeRate(): Any? {
        val response = retryIO { masterDataServices.getExchangeRate(ExchangeRates()) }
        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getSalesManagers(): Any? {
        var filterString = "Active eq 'tYES'"
        if (Preferences.defaultWhs != null) {
            filterString += " and U_whs eq '${Preferences.defaultWhs}'"
        }

        val response = retryIO { masterDataServices.getSalesManagers(filter = filterString) }
        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getSalesManager(managerCode: Long): Any? {
        val response = retryIO { masterDataServices.getSalesManager(managerCode) }
        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getSeries(params: SeriesForPost): Any? {
        val response = retryIO { masterDataServices.getSeries(params) }
        Log.d("SERIES01", response.body().toString())
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error?.error?.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getSeries(params)
                else return error

            } else return error
        }
    }

    override suspend fun getDefaultSeries(params: SeriesForPost): Any? {
        val response = retryIO { masterDataServices.getDefaultSeries(params) }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error?.error?.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getDefaultSeries(params)
                else return error

            } else return error
        }
    }

    override suspend fun getCompanyInfo(): Any? {
        val response = retryIO { masterDataServices.getCompanyInfo() }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error.error.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getCompanyInfo()
                else return error

            } else return error
        }
    }

    private suspend fun reLogin(): Boolean {
        val response = retryIO {
            loginService.requestLogin(
                LoginRequestDto(
                    Preferences.companyDB,
                    Preferences.userPassword,
                    Preferences.userName
                )
            )
        }
        return if (response.isSuccessful) {
            Preferences.sessionID = response.body()?.SessionId
            true
        } else {
            false
        }
    }

}