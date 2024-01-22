package com.example.lorettocashback.domain.interactor

import com.example.lorettocashback.data.entity.businesspartners.*
import com.example.lorettocashback.data.repository.BpRepository
import com.example.lorettocashback.data.repository.BpRepositoryImpl
import com.example.lorettocashback.data.repository.MasterDataRepository
import com.example.lorettocashback.data.repository.MasterDataRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse

interface BpInteractor {

    suspend fun getAllBp(
        filter: String = "",
        bpType: String? = null,
        whsCode: String?,
        onlyWithDebts: Boolean,
    ): List<BusinessPartners>?

    suspend fun getMoreBps(
        filter: String = "",
        skipValue: Int,
        bpType: String? = null,
        whsCode: String?,
        onlyWithDebts: Boolean,
    ): List<BusinessPartners>?

    suspend fun getBpInfo(bpCode: String, whsCode: String?): BusinessPartners?
    suspend fun getBpDebtByShop(cardCode: String = "", whsCode: String = ""): Double?
    suspend fun getBpTotalDebtByShop(
        whsCode: String?,
        bpType: String?,
        onlyWithDebts: Boolean,
    ): Double?

    suspend fun getBusinessPartnerRevision(
        cardCode: String ,
        whsCode: String ,
        dateFrom: String ,
        dateTo: String ,
    ): BpRevision?

    suspend fun addNewBp(bp: BusinessPartnersForPost): BusinessPartners?
    suspend fun checkIfPhoneExists(phone: String?, bpType: String?): String?
    val errorMessage: String?
}

class BpInteractorImpl : BpInteractor {

    private val repository: BpRepository by lazy { BpRepositoryImpl() }
    private val masterDataRepo: MasterDataRepository by lazy { MasterDataRepositoryImpl() }

    override var errorMessage: String? = null

    override suspend fun getAllBp(
        filter: String,
        bpType: String?,
        whsCode: String?,
        onlyWithDebts: Boolean,
    ): List<BusinessPartners>? {
        val response =
            repository.getBps(
                filter = filter,
                bpType = bpType,
                whsCode = whsCode,
                onlyWithDebts = onlyWithDebts
            )

        return if (response is BusinessPartnersVal) {
            response.value
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getMoreBps(
        filter: String,
        skipValue: Int,
        bpType: String?,
        whsCode: String?,
        onlyWithDebts: Boolean,
    ): List<BusinessPartners>? {
        val response = repository.getBps(
            filter = filter,
            skipValue = skipValue,
            bpType = bpType,
            whsCode = whsCode,
            onlyWithDebts = onlyWithDebts
        )

        return if (response is BusinessPartnersVal) {
            response.value
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getBpInfo(bpCode: String, whsCode: String?): BusinessPartners? {
        val response = repository.getBpInfo(bpCode,whsCode)

        return if (response is BusinessPartnersVal) {

            if (!response.value.isNullOrEmpty()){
                response.value[0]
            } else {
                errorMessage = "Бизнес партнер с кодом $bpCode не найден!"
                null
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }

    }

    override suspend fun getBpDebtByShop(cardCode: String, whsCode: String): Double? {
        val response = repository.getBpDebtByShop(cardCode, whsCode)

        return if (response is BusinessPartnerDebtByShopVal) {
            when {
                response.value?.isNotEmpty() == true -> {
                    var debt: Double = 0.0
                    response.value.forEach {
                        debt += it!!.Debt * it.MultiplyBy
                    }
                    debt
                }
                else -> 0.0
            }
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getBpTotalDebtByShop(
        whsCode: String?,
        bpType: String?,
        onlyWithDebts: Boolean,
    ): Double? {
        val response = repository.getBpTotalDebtByShop(whsCode, bpType, onlyWithDebts)

        return if (response is BusinessPartnersVal) {
            when {
                response.value.isNotEmpty() -> {
                    response.value[0].Balance
                }
                else -> 0.0
            }        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getBusinessPartnerRevision(
        cardCode: String,
        whsCode: String,
        dateFrom: String,
        dateTo: String,
    ): BpRevision? {
        val response = repository.getBusinessPartnerRevision(cardCode, whsCode, dateFrom, dateTo)

        return if (response is BpRevisionObjVal) {
            response.transform()
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }


    override suspend fun addNewBp(bp: BusinessPartnersForPost): BusinessPartners? {
        val response = repository.insertNewBp(bp)
        return if (response is BusinessPartners) {
            response
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun checkIfPhoneExists(phone: String?, bpType: String?): String? {
        val response = repository.checkIfPhoneExists(phone, bpType)


        return if (response is BusinessPartnersVal) {
            when {
                response.value.isEmpty() -> {
                    ""
                }
                else -> "${response.value[0].CardCode} - ${response.value[0].CardName}"
            }
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

}