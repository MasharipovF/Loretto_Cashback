package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.data.entity.masterdatas.*
import com.example.lorettocashback.data.entity.series.Series
import com.example.lorettocashback.data.entity.series.SeriesForPost
import com.example.lorettocashback.data.entity.series.SeriesVal
import retrofit2.Response
import retrofit2.http.*

interface MasterDataServices {

    companion object {
        fun get(): MasterDataServices = ServiceBuilder.createService(MasterDataServices::class.java)
    }

    @GET("U_POS_APP_VERSION")
    suspend fun getAppVersion(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$select") fields: String = "Name,U_fromDate,U_link",
        @Query("\$top") top: Int = 1,
        @Query("\$orderby") order: String = "Code desc",
    ): Response<AppVersionVal>


    @GET("Warehouses")
    suspend fun getWarehouses(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$select") fields: String = "WarehouseCode,WarehouseName,EnableBinLocations,DefaultBin,U_inWhs, U_brWhs,U_mainWhs,U_whsType",
        @Query("\$filter") filter: String = "Inactive eq 'tNO'",
        @Query("\$orderby") orderby: String = "WarehouseName asc",
        ): Response<WarehousesVal>


    @GET("UnitOfMeasurementGroups")
    suspend fun getUnitOfMeasureGroups(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$select") fields: String = "AbsEntry,Name,BaseUoM,UoMGroupDefinitionCollection",
    ): Response<UnitOfMeasurementGroupsVal>

    @GET("ItemGroups")
    suspend fun getItemsGroup(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$select") fields: String = "Number,GroupName",
    ): Response<ItemsGroupVal>

    @GET("UnitOfMeasurements")
    suspend fun getUnitOfMeasures(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$select") fields: String = "AbsEntry,Name",
    ): Response<UnitOfMeasurementVal>

    @GET("PriceLists")
    suspend fun getPriceLists(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
    ): Response<PriceListsVal>

    @GET("BusinessPartnerGroups")
    suspend fun getBpGroups(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$filter") filter: String = "Type eq 'bbpgt_VendorGroup' or Type eq 'bbpgt_CustomerGroup'",
    ): Response<BusinessPartnerGroupsVal>

    @GET("SalesPersons")
    suspend fun getSalesManagers(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=500",
        @Query("\$select") fields: String = "SalesEmployeeCode,SalesEmployeeName,Mobile",
        @Query("\$filter") filter: String,
    ): Response<SalesManagersVal>


    @GET("SalesPersons({managerCode})")
    suspend fun getSalesManager(
        @Path("managerCode") managerCode: Long,
        @Query("\$select") fields: String = "SalesEmployeeCode,SalesEmployeeName,Mobile",
    ): Response<SalesManagers>

    @GET("BarCodes")
    suspend fun getLastBarCode(
        @Query("\$filter") filter: String = "startswith(Barcode, '2')",
        @Query("\$orderby") orderby: String = "Barcode desc",
        @Query("\$top") top: String = "1",
    ): Response<BarCodesVal>


    @POST("SBOBobService_GetCurrencyRate")
    suspend fun getExchangeRate(
        @Body body: ExchangeRates,
    ): Response<Double>

    @POST("SeriesService_GetDocumentSeries")
    suspend fun getSeries(
        @Body body: SeriesForPost,
    ): Response<SeriesVal>

    @POST("SeriesService_GetDefaultSeries")
    suspend fun getDefaultSeries(
        @Body body: SeriesForPost,
    ): Response<Series>

    @POST("CompanyService_GetAdminInfo")
    suspend fun getCompanyInfo(): Response<CompanyInfo>


}