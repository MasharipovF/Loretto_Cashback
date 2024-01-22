package com.example.lorettocashback.data.remote.services

import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.core.ServiceBuilder
import com.example.lorettocashback.data.entity.CrossJoin
import com.example.lorettocashback.data.entity.items.*
import com.example.lorettocashback.data.entity.masterdatas.BarCodesVal
import retrofit2.Response
import retrofit2.http.*

interface ItemsService {

    companion object {

        fun get(): ItemsService = ServiceBuilder.createService(ItemsService::class.java)
    }


    @GET("Items")
    suspend fun getFilteredItems(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$select") fields: String = "ItemCode,ItemName,QuantityOnStock,SalesUnit,InventoryUOM,ItemWarehouseInfoCollection",
        @Query("\$filter") filter: String = "",
        @Query("\$orderby") order: String = "ItemName asc",
        @Query("\$skip") skipValue: Int = 0,
    ): Response<ItemsVal>

    @GET("Items")
    suspend fun getFilteredItemsWithPrices(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$select") fields: String = "ItemCode,ItemName,QuantityOnStock,SalesUnit,InventoryUOM,ItemWarehouseInfoCollection,ItemPrices",
        @Query("\$filter") filter: String = "",
        @Query("\$orderby") order: String = "ItemName asc",
        @Query("\$skip") skipValue: Int = 0,
    ): Response<ItemsVal>

    @POST("QueryService_PostQuery")
    suspend fun getFilteredItemsCrossJoin(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=10000",
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Body body: CrossJoin,
    ): Response<ItemsCrossJoinVal>

    @GET("sml.svc/DISCOUNTParameters(P_CardCode='{cardcode}', P_Date='{date}', P_PriceList={pricelist}, P_WhsCode='{whscode}')/DISCOUNT")
    suspend fun getFilteredItemsViaSML(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Path("cardcode") cardcode: String,
        @Path("date") date: String,
        @Path("pricelist") pricelist: Int,
        @Path("whscode") whscode: String,
        @Query("\$select") select: String = "ItemCode,ItemName,QuantityOnStock,BarCode,QuantityOnStockByCurrentWhs,CommittedByCurrentWhs,BinLocationsActivated,SalesUnit,InventoryUOM,SeparateInvoiceItem, NoDiscountItem,Price,DiscountApplied,DiscountType,DiscountLineNum,GroupDiscountPayFor,GroupDiscountForFree,GroupDiscountUpTo",
        @Query("\$filter") filter: String = "",
        @Query("\$skip") skipValue: Int = 0,
    ): Response<ItemsVal>

    @GET("sml.svc/DISCOUNTParameters(P_CardCode='{cardcode}', P_Date='{date}', P_PriceList={pricelist}, P_WhsCode='{whscode}')/DISCOUNT")
    suspend fun getAllItemsViaSML(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=1000000",
        @Path("cardcode") cardcode: String,
        @Path("date") date: String,
        @Path("pricelist") pricelist: Int,
        @Path("whscode") whscode: String,
        @Query("\$select") select: String = "ItemCode,ItemName,QuantityOnStock,BarCode,QuantityOnStockByCurrentWhs,CommittedByCurrentWhs,BinLocationsActivated,SalesUnit,InventoryUOM,SeparateInvoiceItem, NoDiscountItem,Price,DiscountApplied,DiscountType,DiscountLineNum,GroupDiscountPayFor,GroupDiscountForFree,GroupDiscountUpTo",
        @Query("\$filter") filter: String? = null,
        ): Response<ItemsVal>



    @GET("sml.svc/BUNDLE")
    suspend fun getItemBundles(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Header("Prefer") maxPage: String = GeneralConsts.RETURN_ALL_DATA,
        @Query("\$filter") filter: String? = null,
    ): Response<ItemBundleObjVal>

    @GET("Items('{itemcode}')")
    suspend fun getItemInfo(
        @Path("itemcode") itemcode: String,
        @Query("\$select") fields: String = "ItemCode,ItemName,ForeignName,BarCode,ItemsGroupCode,SalesUnit,PurchaseUnit,InventoryUOM,QuantityOnStock,Series,Valid,Frozen,UoMGroupEntry,InventoryUoMEntry,DefaultSalesUoMEntry,DefaultPurchasingUoMEntry,ItemWarehouseInfoCollection,ItemPrices",
    ): Response<Items>

    @GET("BarCodes")
    suspend fun checkIfPhoneBarCodeExists(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Header("Prefer") maxPage: String = "odata.maxpagesize=1",
        @Query("\$select") fields: String = "ItemNo,Barcode",
        @Query("\$filter") filter: String = "",
    ): Response<BarCodesVal>

    @POST("Items")
    suspend fun addNewItem(
        @Body body: ItemsForPost,
    ): Response<Items>


    /*
    {
    "SqlCode": "itemsOnHandByWhs",
    "SqlName": "Items OnHand quantity by Warehouse",
    "SqlText": "SELECT T0.\"ItemCode\", T0.\"OnHand\" FROM OITW T0  INNER JOIN OITM T1 ON T0.\"ItemCode\" = T1.\"ItemCode\" WHERE T0.\"WhsCode\" =:whsCode AND T1.\"validFor\"='Y'"
    }

     */

    @GET("sml.svc/ITEMSONHANDBYWHSParameters(P_CardCode='{cardcode}', P_Date='{date}')/ITEMSONHANDBYWHS")
    suspend fun getItemOnHandByWarehouses(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Header("Prefer") maxPage: String = "odata.maxpagesize=1000000",
        @Path("cardcode") cardcode: String,
        @Path("date") date: String,
        @Query("\$select") fields: String = "ItemCode,OnHand,IsCommited,SeparateInvoiceItem,BinLocationsActivated,WhsCode,WhsName,BarCode,GroupDiscountPayFor,GroupDiscountForFree,GroupDiscountUpTo",
        @Query("\$filter") filter: String = "",
        @Query("\$orderby") order: String = "WhsCode asc",
    ): Response<ItemsOnHandByWhsVal>

    /*
    @GET("SQLQueries('itemsOnHandByWhs')/List")
    suspend fun getAllItemsOnHandByWhs(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=1000000",
        @Query("whsCode") whsCode1: String,
    ): Response<ItemsSQLQueryVal>*/


   /* @GET("BarCodes")
    suspend fun getItemByBarCode(
        @Header("B1S-CaseInsensitive") caseInsensitive: Boolean = true,
        @Query("\$filter") filter: String = "",
        @Query("\$skip") skipValue: Int = 0,
    ): Response<BarCodesVal>*/

    @GET("SQLQueries('itemsByWhsAndPrices')/List")
    suspend fun getAllItemsOnHandByWhsAndPrice(
        @Header("Prefer") maxPage: String = "odata.maxpagesize=100",
        @Query("itemCode") itemCode: String,
        @Query("whsCode") whsCode: String,
        @Query("priceListCode") priceListCode: Int,
    ): Response<ItemsSQLQueryVal>


}