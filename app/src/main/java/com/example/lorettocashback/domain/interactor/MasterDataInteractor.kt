package com.example.lorettocashback.domain.interactor

import android.util.Log
import com.example.lorettocashback.data.entity.masterdatas.*
import com.example.lorettocashback.data.entity.series.Series
import com.example.lorettocashback.data.entity.series.SeriesForPost
import com.example.lorettocashback.data.entity.series.SeriesVal
import com.example.lorettocashback.data.repository.MasterDataRepository
import com.example.lorettocashback.data.repository.MasterDataRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse
import com.example.lorettocashback.domain.mappers.Mappers

interface MasterDataInteractor {
    suspend fun getAppVersion(): AppVersion?
    suspend fun getWarehouses(): List<Warehouses>?
    suspend fun getItemsGroups(): List<ItemsGroup>?
    suspend fun getUomGroups(): List<UnitOfMeasurementGroups>?
    suspend fun getUomsOfUomGroup(uomGroup: UnitOfMeasurementGroups?): List<UnitOfMeasurement>?
    suspend fun getPriceLists(): List<PriceLists>?
    suspend fun getBpGroups(bpGroupType: String?): List<BusinessPartnerGroups>?
    suspend fun getLastBarCode(): String?
    suspend fun getExchangeRate(): Double?
    suspend fun getSalesManagers(): List<SalesManagers>?
    suspend fun getSalesManager(managerCode: Long): SalesManagers?
    suspend fun getSeries(params: SeriesForPost): List<Series>?
    suspend fun getDefaultSeries(params: SeriesForPost): Series?
    suspend fun getCompanyInfo(): CompanyInfo?
    var errorMessage: String?

}

class MasterDataInteractorImpl : MasterDataInteractor {

    override var errorMessage: String? = null

    private val repository: MasterDataRepository by lazy { MasterDataRepositoryImpl() }
    override suspend fun getAppVersion(): AppVersion? {
        val response = repository.getAppVersion()

        return if (response is AppVersionVal) {
            if (response.value.isNullOrEmpty()){
                errorMessage = "Не удается загрузить версию программы!"
                null
            } else {
                response.value[0]
            }
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            Log.wtf("APP_VERSION", errorMessage)
            null
        }







    }

    override suspend fun getWarehouses(): List<Warehouses>? {
        return repository.getWarehouses()?.value
    }

    override suspend fun getItemsGroups(): List<ItemsGroup>? {
        return repository.getItemsGroups()?.value
    }

    override suspend fun getUomGroups(): List<UnitOfMeasurementGroups>? {
        return repository.getUomGroups()?.value
    }

    override suspend fun getUomsOfUomGroup(uomGroup: UnitOfMeasurementGroups?): List<UnitOfMeasurement>? {
        val uoms = repository.getUoms()?.value

        return if (uomGroup?.GroupCode == -1) {
            uoms
        } else {
            Mappers.mapAllUomCodesToNames(uomGroup?.UoMGroupDefinitionCollection, uoms)
        }
    }

    override suspend fun getPriceLists(): List<PriceLists>? {
        return repository.getPriceLists()?.value
    }

    override suspend fun getBpGroups(bpGroupType: String?): List<BusinessPartnerGroups>? {
        return repository.getBpGroups(bpGroupType)?.value
    }

    override suspend fun getLastBarCode(): String? {
        val response = repository.getLastBarCode()
        return if (response is BarCodesVal) {
            if (response.value.isEmpty()) ""
            else response.value[0].Barcode
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getExchangeRate(): Double? {
        val response = repository.getExchangeRate()
        return if (response is Double) {
            response
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getSalesManagers(): List<SalesManagers>? {
        val response = repository.getSalesManagers()
        return if (response is SalesManagersVal) {
            response.value
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getSalesManager(managerCode: Long): SalesManagers? {
        val response = repository.getSalesManager(managerCode)
        return if (response is SalesManagers) {
            response
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getSeries(params: SeriesForPost): List<Series>? {
        Log.d("SERIES0", params.toString())
        val response = repository.getSeries(params)
        return if (response is SeriesVal) {
            response.value
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getDefaultSeries(params: SeriesForPost): Series? {
        val response = repository.getDefaultSeries(params)
        return if (response is Series) {
            response
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getCompanyInfo(): CompanyInfo? {
        val response = repository.getCompanyInfo()
        return if (response is CompanyInfo) {
            response
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

}