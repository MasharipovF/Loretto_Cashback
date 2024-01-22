package com.example.lorettocashback.domain.interactor

import android.util.Log
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.items.*
import com.example.lorettocashback.data.repository.ItemsRepository
import com.example.lorettocashback.data.repository.ItemsRepositoryImpl
import com.example.lorettocashback.data.repository.MasterDataRepository
import com.example.lorettocashback.data.repository.MasterDataRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse
import com.example.lorettocashback.domain.mappers.Mappers

interface ItemsInteractor {

    suspend fun getItems(
        filter: String = "",
        priceListCode: Int? = null,
        whsCode: String?,
        onlyValid: Boolean = false,
        onlyOnHand: Boolean = false,
    ): List<Items>?

    suspend fun getMoreItems(
        filter: String = "",
        skipValue: Int,
        priceListCode: Int? = null,
        whsCode: String?,
        onlyValid: Boolean = false,
        onlyOnHand: Boolean = false,
    ): List<Items>?

    suspend fun getItemsViaSML(
        cardCode: String,
        whsCode: String,
        itemGroupCode: Int? = null,
        priceListCode: Int,
        date: String,
        filter: String = "",
        onlyOnHand: Boolean = false,
        isGeneralItemsList: Boolean,
        skipValue: Int = 0,
    ): List<Items>?

    suspend fun getAllItemsViaSML(
        cardCode: String,
        whsCode: String,
        priceListCode: Int,
        date: String,
        onlyOnHand: Boolean,
    ): List<Items>?


    suspend fun getItemBundles(
        whsCode: String,
    ): List<ItemBundle?>?


    suspend fun getItemInfo(itemcode: String): Items?
    suspend fun addNewItem(item: ItemsForPost): Items?
    suspend fun checkIfBarCodeExists(barcode: String): Boolean?
    var errorMessage: String?

}

class ItemsInteractorImpl : ItemsInteractor {

    private val repository: ItemsRepository by lazy { ItemsRepositoryImpl() }
    private val masterDataRepo: MasterDataRepository by lazy { MasterDataRepositoryImpl() }

    override var errorMessage: String? = null

    override suspend fun getItems(
        filter: String,
        priceListCode: Int?,
        whsCode: String?,
        onlyValid: Boolean,
        onlyOnHand: Boolean,
    ): List<Items>? {

        val response = if (priceListCode != null) {
            repository.getItems(
                filter = filter,
                withPrices = true,
                onlyValid = onlyValid,
                onlyOnHand = onlyOnHand
            )
        } else {
            repository.getItems(filter = filter, onlyValid = onlyValid, onlyOnHand = onlyOnHand)
        }

        return if (response is ItemsVal) {
            var items = response.items

            if (whsCode != null) {
                items = Mappers.setOnHandByCurrentWarehouse(items, whsCode)!!
                if (priceListCode != null) {
                    items = Mappers.setPriceFromChosenPricelist(items, priceListCode)
                }
                Log.d("DEFAULTWHS", whsCode)
            }
            items
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }

    }

    override suspend fun getMoreItems(
        filter: String,
        skipValue: Int,
        priceListCode: Int?,
        whsCode: String?,
        onlyValid: Boolean,
        onlyOnHand: Boolean,
    ): List<Items>? {
        val response = if (priceListCode != null) {
            repository.getItems(
                filter = filter,
                skipValue = skipValue,
                withPrices = true,
                onlyValid = onlyValid,
                onlyOnHand = onlyOnHand
            )
        } else {
            repository.getItems(
                filter = filter,
                skipValue = skipValue,
                onlyValid = onlyValid,
                onlyOnHand = onlyOnHand
            )
        }


        return if (response is ItemsVal) {
            var items = response.items

            if (whsCode != null) {
                items = Mappers.setOnHandByCurrentWarehouse(items, whsCode)!!
                if (priceListCode != null) {
                    items = Mappers.setPriceFromChosenPricelist(items, priceListCode)
                }
                Log.d("DEFAULTWHS", whsCode)
            }
            items
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }

    }

    override suspend fun getItemsViaSML(
        cardCode: String,
        whsCode: String,
        itemGroupCode: Int?,
        priceListCode: Int,
        date: String,
        filter: String,
        onlyOnHand: Boolean,
        isGeneralItemsList: Boolean,

        skipValue: Int,
    ): List<Items>? {
        val response =
            repository.getItemsViaSML(
                cardCode = cardCode,
                whsCode = whsCode,
                date = date,
                priceListCode = priceListCode,
                itemGroupCode = itemGroupCode,
                filter = filter,
                onlyOnHand = onlyOnHand,
                isGeneralItemsList = isGeneralItemsList,
                skipValue = skipValue
            )

        return if (response is ItemsVal) {
            val items = response.items

            items.forEach {
                it.DiscountedPrice = if (it.DiscountType == GeneralConsts.DISCOUNT_TYPE_PROHIBIT_DISCOUNT) {
                    Preferences.currencyRate * it.Price
                } else {
                    Preferences.currencyRate * (it.Price * (100 - it.DiscountApplied)) / 100
                }
                it.Price = Preferences.currencyRate * it.Price
            }

            items
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }

    override suspend fun getAllItemsViaSML(
        cardCode: String,
        whsCode: String,
        priceListCode: Int,
        date: String,
        onlyOnHand: Boolean,
    ): List<Items>? {
        val response =
            repository.getAllItemsViaSML(
                cardCode = cardCode,
                whsCode = whsCode,
                date = date,
                priceListCode = priceListCode,
                onlyOnHand = onlyOnHand
            )

        return if (response is ItemsVal) {
            val items = response.items

            items.forEach {
                it.DiscountedPrice = if (it.DiscountType == GeneralConsts.DISCOUNT_TYPE_PROHIBIT_DISCOUNT) {
                    Preferences.currencyRate * it.Price
                } else {
                    Preferences.currencyRate * (it.Price * (100 - it.DiscountApplied)) / 100
                }
                it.Price = Preferences.currencyRate * it.Price

            }

            items
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }


    override suspend fun getItemBundles(whsCode: String): List<ItemBundle?>? {
        val response =
            repository.getItemBundles(whsCode)
        return if (response is ItemBundleObjVal) {
            response.transform()
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }


    /*private suspend fun mapBarcodeWithItemInfo(itemcode: String, priceListCode: Int): List<Items>? {
        val resultList = arrayListOf<Items>()
        val response = repository.getItemsWithPricesSQLQuery(
            itemCode = itemcode,
            whsCode = Preferences.defaultWhs!!,
            priceListCode = priceListCode
        )

        Log.d("BARCODE", response.toString())

        return if (response is ItemsSQLQueryVal) {
            response.value!!.forEach {
                val item = Items(
                    ItemName = it!!.itemName,
                    TotalOnHand = it.totalOnHand!!,
                    OnHandCurrentWhs = it.onHand!!,
                    SalesUnit = it.salesUnitMeasure,
                    Price = it.price!! * Preferences.currencyRate
                )
                resultList.add(item)
            }
            resultList
        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }
    }*/


    override suspend fun getItemInfo(itemcode: String): Items? {
        val result = repository.getItemInfo(itemcode)
        val whs = masterDataRepo.getWarehouses()?.value
        val itemsGroups = masterDataRepo.getItemsGroups()?.value
        val uomGroups = masterDataRepo.getUomGroups()?.value

        result?.ItemsGroupName =
            Mappers.mapItemGroupCodeToName(itemsGroups, result?.ItemsGroupCode)
        result?.UoMGroupName =
            Mappers.mapUomGroupCodeToName(uomGroups, result?.UoMGroupEntry)
        result?.ItemWarehouseInfoCollection =
            Mappers.mapWhsCodeToWhsName(whs, result?.ItemWarehouseInfoCollection)
        return result
    }

    override suspend fun addNewItem(item: ItemsForPost): Items? {
        return repository.addNewItem(item)
    }

    override suspend fun checkIfBarCodeExists(barcode: String): Boolean? {
        val response = repository.checkIfBarCodeExists(barcode)?.value
        return when {
            response == null -> {
                null
            }
            response.isEmpty() -> {
                false
            }
            else -> true
        }
    }

}