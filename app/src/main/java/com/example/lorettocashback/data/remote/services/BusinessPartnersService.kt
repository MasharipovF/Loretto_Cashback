package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.data.entity.businesspartners.*
import retrofit2.Response
import retrofit2.http.*

interface BusinessPartnersService {

    companion object {

        fun get(): BusinessPartnersService =
            ServiceBuilder.createService(BusinessPartnersService::class.java)
    }

    @GET("sml.svc/CASHBACK_USERS")
    suspend fun getUserData(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$select") fields: String? = null,
        @Query("\$filter") filter: String?=null,
    ): Response<BusinessPartnersVal>

//    "CardCode,CardName,Phone1"

    @GET("BusinessPartners")
    suspend fun getFilteredBps(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$select") fields: String = "CardCode,CardName,CardType,Currency,GroupCode,CurrentAccountBalance,Phone1,Phone2,ShipToDefault,CreditLimit,MaxCommitment,PriceListNum,Valid,Frozen,Series",
        @Query("\$filter") filter: String = "",
        @Query("\$orderby") order: String = "CardName asc",
        @Query("\$skip") skipValue: Int = 0
    ): Response<BusinessPartnersVal>

    // THROUGH SEMANTIC LAYER
    @GET("sml.svc/BPLIST_DEBTBYSHOP")
    suspend fun getFilteredBpsViaSML(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$filter") filter: String = "",
        @Query("\$orderby") order: String = "CardName asc",
        @Query("\$skip") skipValue: Int = 0
    ): Response<BusinessPartnersVal>

    @GET("sml.svc/BPLIST_DEBTBYSHOP")
    suspend fun getBusinessPartnerTotalDebtByShop(
        @Query("\$filter") filter: String?=null,
        @Query("\$apply") aggregation: String ,
    ): Response<BusinessPartnersVal>

    @GET("BusinessPartners")
    suspend fun checkIfPhoneExists(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Header("Prefer") maxPage: String = "odata.maxpagesize=1",
        @Query("\$select") fields: String = "CardCode,CardName,Phone1",
        @Query("\$filter") filter: String = ""
    ): Response<BusinessPartnersVal>


    @GET("BusinessPartners('{bpCode}')")
    suspend fun getBpInfo(
        @Path("bpCode") bpCode: String,
        @Query("\$select") fields: String = "CardCode,CardName,CardType,Currency,GroupCode,CurrentAccountBalance,OpenDeliveryNotesBalance,OpenOrdersBalance,FederalTaxID,Phone1,Phone2,ShipToDefault,CreditLimit,MaxCommitment,PriceListNum,Valid,Frozen,Series,BPAddresses",
    ): Response<BusinessPartners>

    @POST("BusinessPartners")
    suspend fun insertNewBp(
        @Body bp: BusinessPartnersForPost
    ): Response<BusinessPartners>


    /*
    THE NEW ONE
    {
    "SqlCode": "debtByShop",
    "SqlName": "BpDebtByShop",
    "SqlText": "SELECT T0.U_whs AS WhsCode, 1 AS MultiplyBy, SUM(T0.DocTotal) AS DocTotal, SUM(T0.PaidToDate) AS PaidToDate FROM OINV T0 WHERE T0.DocType='I' AND T0.DocStatus='O' AND T0.CardCode =:cardCode1 GROUP BY T0.U_whs UNION ALL SELECT T0.CounterRef, -1, SUM(T0.OpenBal),0 FROM ORCT T0 WHERE T0.OpenBal<>0 AND T0.CardCode =:cardCode2 GROUP BY T0.CounterRef UNION ALL SELECT T0.U_whs, 1, SUM(T0.DocTotal) AS DocTotal, SUM(T0.PaidToDate) AS PaidToDate FROM OINV T0  INNER JOIN INV1 T1 ON T0.DocEntry = T1.DocEntry WHERE T0.DocType='S' AND T0.DocStatus='O' AND T0.CardCode =:cardCode3 GROUP BY T0.U_whs"
    }
     */

    // ДОЛГИ КЛИЕНТОВ ПО МАГАЗИНУ
    @GET("SQLQueries('debtByShop')/List")
    suspend fun getBusinessPartnerDebtByShop(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=1000",
        @Query("cardCode1") cardCode1: String,
        @Query("cardCode2") cardCode2: String,
        @Query("cardCode3") cardCode3: String,
        @Query("cardCode4") cardCode4: String,
        @Query("whsCode1") whsCode1: String,
        @Query("whsCode2") whsCode2: String,
        @Query("whsCode3") whsCode3: String,
        @Query("whsCode4") whsCode4: String,
    ): Response<BusinessPartnerDebtByShopVal>


    @GET("sml.svc/BP_REVISIONParameters(P_DateFrom='{datefrom}', P_DateTo='{dateto}', P_WhsCode='{whscode}', P_CardCode='{cardcode}')/BP_REVISION")
    suspend fun getBusinessPartnerRevision(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Header("Prefer") maxPage: String = GeneralConsts.RETURN_ALL_DATA,
        @Path("cardcode") cardcode: String,
        @Path("datefrom") datefrom: String,
        @Path("dateto") dateto: String,
        @Path("whscode") whscode: String,
    ): Response<BpRevisionObjVal>
}