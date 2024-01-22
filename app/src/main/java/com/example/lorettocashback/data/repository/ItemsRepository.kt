package com.example.lorettocashback.data.repository

import android.util.Log
import com.example.lorettocashback.core.ErrorCodeEnums
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.CrossJoin
import com.example.lorettocashback.data.entity.documents.DocumentLines
import com.example.lorettocashback.data.entity.items.Items
import com.example.lorettocashback.data.entity.items.ItemsCrossJoinVal
import com.example.lorettocashback.data.entity.items.ItemsForPost
import com.example.lorettocashback.data.entity.items.ItemsVal
import com.example.lorettocashback.data.entity.masterdatas.BarCodesVal
import com.example.lorettocashback.data.remote.services.ItemsService
import com.example.lorettocashback.data.remote.services.LoginService
import com.example.lorettocashback.domain.dto.login.LoginRequestDto
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.retryIO

interface ItemsRepository {
    suspend fun getItems(
        filter: String = "",
        skipValue: Int = 0,
        withPrices: Boolean = false,
        onlyValid: Boolean = false,
        onlyOnHand: Boolean = false,
    ): ItemsVal?

    suspend fun getItemsViaSML(
        cardCode: String,
        whsCode: String,
        date: String,
        priceListCode: Int,
        itemGroupCode: Int?,
        filter: String = "",
        onlyOnHand: Boolean = false,
        isGeneralItemsList: Boolean,
        skipValue: Int = 0,
    ): Any?

    suspend fun getAllItemsViaSML(
        cardCode: String,
        whsCode: String,
        date: String,
        priceListCode: Int,
        onlyOnHand: Boolean
    ): Any?


    suspend fun getItemsCrossJoin(
        filters: List<String> = listOf(),
        skipValue: Int = 0,
        whsCode: String,
        onlyValid: Boolean = false,
    ): ItemsCrossJoinVal?

    /*suspend fun getItemsSQLQuery(
        whsCode: String
    ): Any?*/

    suspend fun getItemsListOnHandByWarehouse(
        whsCode: String,
        filter: String = "",
        cardCode: String? = null,
        date: String,
    ): Any?

    suspend fun getItemsWithPricesSQLQuery(
        itemCode: String,
        whsCode: String,
        priceListCode: Int,
    ): Any?

    suspend fun getItemBundles(
        whsCode: String,
    ): Any?

    suspend fun getItemInfo(itemcode: String): Items?
    suspend fun addNewItem(item: ItemsForPost): Items?
    suspend fun checkIfBarCodeExists(barcode: String): BarCodesVal?
}

class ItemsRepositoryImpl(
    private val itemsService: ItemsService = ItemsService.get(),
    private val loginService: LoginService = LoginService.get(),

    ) : ItemsRepository {


    override suspend fun getItems(
        filter: String,
        skipValue: Int,
        withPrices: Boolean,
        onlyValid: Boolean,
        onlyOnHand: Boolean
        ): ItemsVal? {

        var filterStringBuilder =
            if (onlyValid) "(contains(ItemCode, '$filter') or contains(ItemName, '$filter') or contains(BarCode,'$filter')) and ItemType eq 'itItems' and Valid eq 'tYES'"
            else "(contains(ItemCode, '$filter') or contains(ItemName, '$filter') or contains(BarCode,'$filter')) and ItemType eq 'itItems'"

        if (onlyOnHand){
            filterStringBuilder+=" and QuantityOnStock gt 0"
        }

        val response = retryIO {
            if (withPrices)
                itemsService.getFilteredItemsWithPrices(
                    filter = filterStringBuilder,
                    skipValue = skipValue
                )
            else
                itemsService.getFilteredItems(filter = filterStringBuilder, skipValue = skipValue)
        }

        Log.d("ITEMS", "response  " + response.toString())

        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.d(
                "USERDEFAULTS",
                response.errorBody()!!.string()
            )
            null
        }

    }

    override suspend fun getItemsViaSML(
        cardCode: String,
        whsCode: String,
        date: String,
        priceListCode: Int,
        itemGroupCode: Int?,
        filter: String,
        onlyOnHand: Boolean,
        isGeneralItemsList: Boolean,
        skipValue: Int,
    ): Any? {

        //TODO ITEM TYPE IS ITEM


        var filterStringBuilder =
            "(contains(ItemCode, '$filter') or contains(ItemName, '$filter') or contains(BarCode,'$filter'))"

        if (onlyOnHand){
            filterStringBuilder += if (isGeneralItemsList){
                " and QuantityOnStock gt 0"
            } else {
                " and QuantityOnStockByCurrentWhs gt 0"
            }
        }

        if(itemGroupCode!=null){
            filterStringBuilder+=" and ItemsGroupCode eq $itemGroupCode"
        }

        val response = retryIO {
            itemsService.getFilteredItemsViaSML(
                cardcode = cardCode,
                whscode = whsCode,
                pricelist = priceListCode,
                date = date,
                filter = filterStringBuilder,
                skipValue = skipValue
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getAllItemsViaSML(
        cardCode: String,
        whsCode: String,
        date: String,
        priceListCode: Int,
        onlyOnHand: Boolean
    ): Any? {

        val response = retryIO {
            itemsService.getAllItemsViaSML(
                cardcode = cardCode,
                whscode = whsCode,
                pricelist = priceListCode,
                date = date,
                filter = if (onlyOnHand) "QuantityOnStockByCurrentWhs gt 0" else null
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }


    override suspend fun getItemsCrossJoin(
        filters: List<String>,
        skipValue: Int,
        whsCode: String,
        onlyValid: Boolean,
    ): ItemsCrossJoinVal? {

        var itemsFilterString = ""
        var resultList = arrayListOf<DocumentLines>()

        filters.forEachIndexed { index, item ->
            itemsFilterString += "Items/ItemCode eq '${item}'"
            if (index < filters.size - 1 && itemsFilterString.isNotEmpty()) itemsFilterString += " or "
        }

        Log.d("SALESORDER", "filterstring: $itemsFilterString")

        val queryPath = "\$crossjoin(Items,Items/ItemWarehouseInfoCollection)"
        val expand =
            "\$expand=Items(\$select=ItemCode),Items/ItemWarehouseInfoCollection(\$select=WarehouseCode,InStock,Committed)"

        val filterString =
            if (onlyValid) "\$filter=Items/ItemCode eq Items/ItemWarehouseInfoCollection/ItemCode and Items/ItemWarehouseInfoCollection/WarehouseCode eq '$whsCode' and Items/ItemType eq 'itItems' and Items/Valid eq 'tYES' and ($itemsFilterString)"
            else "\$filter=Items/ItemCode eq Items/ItemWarehouseInfoCollection/ItemCode and Items/ItemWarehouseInfoCollection/WarehouseCode eq '$whsCode' and Items/ItemType eq 'itItems' and ($itemsFilterString)"

        val skip = "\$skip=$skipValue"
        val queryOption = "$expand&$filterString&$skip"

        val response = retryIO {
            itemsService.getFilteredItemsCrossJoin(
                body = CrossJoin(
                    queryOption = queryOption,
                    queryPath = queryPath
                )
            )
        }

        return if (response.isSuccessful) {
            response.body() as ItemsCrossJoinVal
        } else {
            Log.d(
                "USERDEFAULTS",
                response.errorBody()!!.string()
            )
            null
        }
    }

    /*override suspend fun getItemsSQLQuery(whsCode: String): Any? {
        val response = retryIO { itemsService.getAllItemsOnHandByWhs(whsCode1 = "'$whsCode'") }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error?.error?.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getItemsSQLQuery(whsCode)
                else return error

            } else return error
        }
    }*/

    override suspend fun getItemsListOnHandByWarehouse(
        whsCode: String,
        filterString: String,
        cardCode: String?,
        date: String,
    ): Any? {

        val filterStringBuilder =
            "WhsCode eq '$whsCode' and (contains(ItemName, '$filterString') or contains(BarCode, '$filterString'))"

        val response =
            retryIO {
                itemsService.getItemOnHandByWarehouses(filter = filterStringBuilder,
                    cardcode = cardCode.toString(),
                    date = date)
            }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error?.error?.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getItemsListOnHandByWarehouse(whsCode, filterString, cardCode, date)
                else return error

            } else return error
        }
    }


    override suspend fun getItemsWithPricesSQLQuery(
        itemCode: String,
        whsCode: String,
        priceListCode: Int,
    ): Any? {
        Log.d("BARCODE", "ITEM: $itemCode, whs: $whsCode, price: $priceListCode")
        val response = retryIO {
            itemsService.getAllItemsOnHandByWhsAndPrice(
                itemCode = "'$itemCode'",
                whsCode = "'$whsCode'",
                priceListCode = priceListCode
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error.error.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getItemsWithPricesSQLQuery(itemCode, whsCode, priceListCode)
                else return error

            } else return error
        }
    }

    override suspend fun getItemBundles(whsCode: String): Any? {
        val response = retryIO {
            itemsService.getItemBundles(
                filter = "WhsCode eq '$whsCode'"
            )
        }
        return if (response.isSuccessful) {
            response.body()
        } else {
            val error = ErrorUtils.errorProcess(response)
            if (error.error.code == ErrorCodeEnums.SESSION_TIMEOUT.code) {

                val isLoggedIn = reLogin()
                if (isLoggedIn) getItemBundles(whsCode)
                else return error

            } else return error
        }
    }


    override suspend fun getItemInfo(itemcode: String): Items? {
        val response = retryIO {
            itemsService.getItemInfo(itemcode = itemcode)
        }

        return if (response.isSuccessful) {
            response.body()
        } else {

            Log.d(
                "USERDEFAULTS",
                response.errorBody()!!.string()
            )
            null
        }
    }

    override suspend fun addNewItem(item: ItemsForPost): Items? {
        val response = retryIO { itemsService.addNewItem(item) }
        if (response.isSuccessful) {
            Log.d("ITEMINSERT", response.body().toString())
        } else Log.d("ITEMINSERTERROR", response.errorBody()!!.string())
        return response.body()
    }

    override suspend fun checkIfBarCodeExists(barcode: String): BarCodesVal? {
        val barcode = "Barcode eq '$barcode'"
        val response = retryIO { itemsService.checkIfPhoneBarCodeExists(filter = barcode) }
        return if (response.isSuccessful) {
            Log.d(
                "BPDEFAULTS",
                response.body().toString()
            )
            response.body()

        } else {
            Log.d(
                "BPDEFAULTS",
                response.errorBody()!!.string()
            )
            null
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