package com.example.lorettocashback.data.repository

import android.util.Log
import com.example.lorettocashback.core.ErrorCodeEnums
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.entity.businesspartners.BusinessPartnersForPost
import com.example.lorettocashback.data.remote.services.BusinessPartnersService
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.LoginUtils.reLogin
import com.example.lorettocashback.util.retryIO

interface BpRepository {
    suspend fun getBps(
        filter: String = "",
        skipValue: Int = 0,
        bpType: String? = null,
        whsCode: String? = null,
        onlyWithDebts: Boolean,
    ): Any? //BusinessPartnersVal?

    suspend fun getBpInfo(bpCode: String, whsCode: String? = null,): Any? //BusinessPartners?
    suspend fun getBpDebtByShop(
        cardCode: String = "",
        whsCode: String = "",
    ): Any? //BusinessPartnersDebtForPostVal?

    suspend fun getBpTotalDebtByShop(
        whsCode: String? = null,
        bpType: String? = null,
        onlyWithDebts: Boolean,
    ): Any? //BusinessPartnersDebtForPostVal?

    suspend fun getBusinessPartnerRevision(
        cardCode: String ,
        whsCode: String ,
        dateFrom: String ,
        dateTo: String ,
    ): Any?

    suspend fun insertNewBp(bpCode: BusinessPartnersForPost): Any? //BusinessPartners?
    suspend fun checkIfPhoneExists(phone: String?, bpType: String?): Any? //BusinessPartnersVal?
}

class BpRepositoryImpl(
    private val bpService: BusinessPartnersService = BusinessPartnersService.get(),
) :
    BpRepository {


    override suspend fun getBps(
        filter: String,
        skipValue: Int,
        bpType: String?,
        whsCode: String?,
        onlyWithDebts: Boolean,
    ): Any? {

        // IF WE USE SEMANTIC LAYER, WE NEED TO RENAME THE FIELDS tYES to Y / cCustomer to C and so on
        val validity = if (whsCode != null) "Y" else "tYES"
        val cardType: String? = if (whsCode != null) {
            if (bpType == GeneralConsts.BP_TYPE_CUSTOMER) GeneralConsts.BP_TYPE_CUSTOMER_SML else GeneralConsts.BP_TYPE_SUPPLIER_SML
        } else bpType
        val onlyDebts =
            if (whsCode != null) " and CurrentAccountBalanceByShop ne 0" else " and CurrentAccountBalance ne 0"

        var filterStringBuilder =
            if (cardType == null) "(contains(CardName, '$filter') or contains(Phone1, '$filter')) and Valid eq '$validity'"
            else "(contains(CardName, '$filter') or contains(Phone1, '$filter')) and Valid eq '$validity' and CardType eq '$cardType'"

        if (whsCode != null) {
            filterStringBuilder += " and WhsCode eq '$whsCode'"
        }

        if (onlyWithDebts) {
            filterStringBuilder += onlyDebts
        }

        Log.d("SML", filterStringBuilder)
        val response = retryIO {
            if (whsCode != null)
                bpService.getFilteredBpsViaSML(filter = filterStringBuilder, skipValue = skipValue)
            else
                bpService.getFilteredBps(filter = filterStringBuilder, skipValue = skipValue)
        }


        return if (response.isSuccessful) {
            Log.wtf("BUSINESS=PARTNERS", response.body().toString())
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }


    override suspend fun getBpInfo(bpCode: String, whsCode: String?): Any? {
        var filterString = "CardCode eq '$bpCode'"

        if (whsCode != null) {
            filterString += " and WhsCode eq '$whsCode'"
        }


        Log.wtf("BUSINESS=PARTNERS", filterString)

        val response = retryIO { bpService.getFilteredBpsViaSML(filter = filterString) }

        return if (response.isSuccessful) {
            Log.wtf("BUSINESS=PARTNERS", response.body().toString())

            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getBpDebtByShop(cardCode: String, whsCode: String): Any? {

        val response = retryIO {
            bpService.getBusinessPartnerDebtByShop(
                cardCode1 = "'$cardCode'",
                cardCode2 = "'$cardCode'",
                cardCode3 = "'$cardCode'",
                cardCode4 = "'$cardCode'",
                whsCode1 = "'$whsCode'",
                whsCode2 = "'$whsCode'",
                whsCode3 = "'$whsCode'",
                whsCode4 = "'$whsCode'",
            )
        }

        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getBpTotalDebtByShop(
        whsCode: String?,
        bpType: String?,
        onlyWithDebts: Boolean,
    ): Any? {

        /**
         * https://212.83.152.252:50000/b1s/v2/sml.svc/BPLIST_DEBTBYShopS?
         * $filter=WhsCode eq '1'&
         * $apply=groupby((WhsCode),aggregate(CurrentAccountBalanceByShop with sum as CurrentAccountBalance))
         */


        var filterStringBuilder = "WhsCode eq '$whsCode'"
        val applyString =
            "groupby((WhsCode),aggregate(CurrentAccountBalanceByShop with sum as CurrentAccountBalance))"


        val validity = "Y"
        val cardType: String? =
            if (bpType == GeneralConsts.BP_TYPE_CUSTOMER) GeneralConsts.BP_TYPE_CUSTOMER_SML else GeneralConsts.BP_TYPE_SUPPLIER_SML


        filterStringBuilder +=
            if (cardType == null) " and Valid eq '$validity'"
            else " and Valid eq '$validity' and CardType eq '$cardType'"


        val onlyDebts = " and CurrentAccountBalanceByShop ne 0"
        if (onlyWithDebts) {
            filterStringBuilder += onlyDebts
        }

        Log.wtf("DEBTBYSHOP", filterStringBuilder.toString())
        Log.wtf("DEBTBYSHOP", applyString.toString())


        val response = retryIO {
            bpService.getBusinessPartnerTotalDebtByShop(
                filter = filterStringBuilder,
                aggregation = applyString
            )
        }

        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error.error.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getBpTotalDebtByShop(whsCode, bpType, onlyWithDebts)
                else return error

            } else return error
        }
    }

    override suspend fun getBusinessPartnerRevision(
        cardCode: String,
        whsCode: String,
        dateFrom: String,
        dateTo: String,
    ): Any? {
        val response = retryIO {
            bpService.getBusinessPartnerRevision(
                cardcode = cardCode,
                whscode = whsCode,
                datefrom = dateFrom,
                dateto = dateTo
            )
        }

        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error.error.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getBusinessPartnerRevision(cardCode, whsCode, dateFrom, dateTo)
                else return error

            } else return error
        }
    }


    override suspend fun insertNewBp(bpCode: BusinessPartnersForPost): Any? {
        val response = retryIO { bpService.insertNewBp(bpCode) }

        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun checkIfPhoneExists(phone: String?, bpType: String?): Any? {
        val phoneNumber = if (bpType == null) {
            "Phone1 eq '$phone'"
        } else "Phone1 eq '$phone' and CardType eq '$bpType'"

        val response = retryIO { bpService.checkIfPhoneExists(filter = phoneNumber) }

        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

}