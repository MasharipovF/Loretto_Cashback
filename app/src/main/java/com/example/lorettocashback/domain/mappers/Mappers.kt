package com.example.lorettocashback.domain.mappers

import android.util.Log
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.documents.Document
import com.example.lorettocashback.data.entity.documents.DocumentLines
import com.example.lorettocashback.data.entity.documents.DocumentLinesForPost
import com.example.lorettocashback.data.entity.items.ItemPrices
import com.example.lorettocashback.data.entity.items.ItemWarehouseInfo
import com.example.lorettocashback.data.entity.items.Items
import com.example.lorettocashback.data.entity.items.ItemsCrossJoin
import com.example.lorettocashback.data.entity.masterdatas.*
import com.example.lorettocashback.util.Utils
import com.google.gson.Gson


object Mappers {

    fun getDocLinesWithBaseDoc(baseDoc: Document, isViewMode: Boolean): ArrayList<DocumentLines> {
        val resultList = ArrayList<DocumentLines>()
        Log.wtf("BASEDOC", Gson().toJson(baseDoc))
        for (line in baseDoc.DocumentLines) {
            line.BaseEntry = baseDoc.DocEntry
            line.BaseType = Utils.getObjectCode(baseDoc.DocObjectCode!!)
            line.BaseLine = line.LineNum

            if (line.LineStatus == GeneralConsts.DOC_STATUS_CLOSED) {
                line.UserQuantity = line.Quantity
                line.MaxQuantity = line.Quantity
                line.InitialQuantity = line.Quantity ?: 0.0
            } else {
                line.UserQuantity = line.RemainingOpenQuantity
                line.Quantity = line.RemainingOpenQuantity
                line.MaxQuantity = line.RemainingOpenQuantity
                line.InitialQuantity = line.RemainingOpenQuantity ?: 0.0
            }

            line.BaseQuantity = line.Quantity
            line.DiscountBase = line.DiscountPercent
            line.UserPriceAfterVATUSD = line.PriceAfterVATUSD

            // WHEN WE OPEN OLD INVOICES, FOR EXAMPLE FOR PRINTING, WE NEED TO GET PRICE FOR THAT PERIOD OF TIME
            // BUT WHEN WE LOAD SALES ORDER, WEE NEED TO GET NEW PRICE ACCORDING TO NEW CURRENCY RATE
            if (isViewMode) {
                var currency: Double =
                    Utils.roundDoubleValue(line.PriceAfterVATUZS?.div(line.PriceAfterVATUSD ?: 0.0)
                        ?: 0.0)

                if (line.PriceAfterVATUSD==null || line.PriceAfterVATUSD==0.0)
                    currency = 0.0


                line.BasePriceUZS = line.BasePriceUSD.times(currency ?: 0.0)?:0.0
                line.UserPriceAfterVATUZS = line.PriceAfterVATUSD?.times(currency ?: 0.0)?:0.0
            } else {
                line.BasePriceUZS = line.BasePriceUSD.times(Preferences.currencyRate)?:0.0
                line.UserPriceAfterVATUZS = line.PriceAfterVATUSD?.times(Preferences.currencyRate)?:0.0
            }

            resultList.add(line)
        }

        Log.wtf("LOADEDITEMS", resultList.toString())
        return resultList
    }

    fun mapDocLinesToDocLinesForPost(
        docLines: List<DocumentLines>,
        isCopyFrom: Boolean,
        isUpdatingSalesOrder: Boolean = false
    ): List<DocumentLinesForPost> {
        Log.d("INSERTINVOICE", docLines.toString())
        val resultList = ArrayList<DocumentLinesForPost>()
        docLines.forEachIndexed { index, line ->
            // IF WE ARE NOT UPDATING, THEN WE DO NOT NEED TO ADD CLOSED LINE TO THE DOCUMENT
            if (line.LineStatus == GeneralConsts.DOC_STATUS_CLOSED  && isCopyFrom) {

            } else {
                val updateLine = DocumentLinesForPost(
                    LineNum = if (isUpdatingSalesOrder) line.LineNum else index.toLong(),
                    ItemCode = line.ItemCode,
                    ItemName = line.ItemName,
                    PriceAfterVAT = line.UserPriceAfterVATUSD,
                    UserPriceAfterVATUZS = line.UserPriceAfterVATUZS,
                    UnitPrice = line.BasePriceUSD,
                    DiscountPercent = line.DiscountPercent,
                    WarehouseCode = line.WarehouseCode,
                    DiscountType = line.DiscountType,
                    BundleCode = line.BundleCode,
                    IsFreeItem = line.IsFreeItem,
                    OINVDocEntry = line.OINVDocEntry
                )

                updateLine.Quantity =
                    line.BaseQuantity!! + line.UserQuantity!! - line.InitialQuantity

                if (isCopyFrom) {
                    updateLine.BaseLine = line.BaseLine
                    updateLine.BaseType = line.BaseType
                    updateLine.BaseEntry = line.BaseEntry
                } else {
                    updateLine.BaseLine = null
                    updateLine.BaseType = null
                    updateLine.BaseEntry = null
                }
                resultList.add(updateLine)
            }


        }

        Log.d("INSERTINVOICE", resultList.toString())

        return resultList
    }



    fun mapItemsCrossJoinToItems(sourceList: List<ItemsCrossJoin>?): List<Items> {
        val resultList: ArrayList<Items> = arrayListOf()
        if (sourceList != null) {
            for (value in sourceList) {
                val tempItem = value.items
                tempItem.OnHandCurrentWhs = value.itemsItemWarehouseInfoCollection.InStock
                resultList.add(tempItem)
            }
        }
        return resultList
    }

    fun setOnHandByCurrentWarehouse(sourceList: List<Items>?, searchFor: String?): List<Items>? {
        val resultList: ArrayList<Items>? = arrayListOf()
        if (sourceList != null) {

            for (sourceItem in sourceList) {

                val itemWhsInfoList = sourceItem.ItemWarehouseInfoCollection
                for (itemWhsInfo in itemWhsInfoList) {
                    if (itemWhsInfo.WarehouseCode == searchFor) {
                        sourceItem.OnHandCurrentWhs = itemWhsInfo.InStock
                        sourceItem.CommittedCurrentWhs = itemWhsInfo.Committed
                        break
                    }
                }

                resultList?.add(sourceItem)

            }

        }

        return resultList
    }

    fun setPriceFromChosenPricelist(
        sourceList: List<Items>?,
        priceListForSearch: Int?,
        currencyForSearch: String? = GeneralConsts.PRIMARY_CURRENCY,
    ): List<Items> {
        val resultList: ArrayList<Items> = arrayListOf()
        if (sourceList != null) {

            for (sourceItem in sourceList) {

                val itemPricesList = sourceItem.ItemPrices
                for (itemPrice in itemPricesList) {
                    if (itemPrice.priceList == priceListForSearch) {

                        when (currencyForSearch) {
                            itemPrice.currency -> {
                                sourceItem.Price =
                                    itemPrice.price * Preferences.currencyRate
                                sourceItem.DiscountedPrice =
                                    itemPrice.price * Preferences.currencyRate
                            }

                            itemPrice.additionalCurrency1 -> {
                                sourceItem.Price =
                                    itemPrice.additionalPrice1 * Preferences.currencyRate
                                sourceItem.DiscountedPrice =
                                    itemPrice.additionalPrice1 * Preferences.currencyRate
                            }

                            itemPrice.additionalCurrency2 -> {
                                sourceItem.Price =
                                    itemPrice.additionalPrice2 * Preferences.currencyRate
                                sourceItem.DiscountedPrice =
                                    itemPrice.additionalPrice2 * Preferences.currencyRate
                            }

                            else -> sourceItem.Price = 0.0
                        }
                        break
                    }
                }
                resultList.add(sourceItem)
            }

        }
        return resultList
    }


    fun mapWhsCodeToWhsName(
        sourceList: List<Warehouses>?,
        itemWarehouseInfo: List<ItemWarehouseInfo>?,
    ): List<ItemWarehouseInfo> {

        val resultList: ArrayList<ItemWarehouseInfo> = arrayListOf()


        if (sourceList != null && itemWarehouseInfo != null) {

            for (itemWhsInfo in itemWarehouseInfo) {
                for (whs in sourceList) {
                    if (itemWhsInfo.WarehouseCode == whs.WarehouseCode) {
                        itemWhsInfo.WarehouseName = whs.WarehouseName
                        resultList.add(itemWhsInfo)
                        break
                    }

                }

            }

        }
        return resultList as ArrayList<ItemWarehouseInfo>
    }

    fun mapItemGroupCodeToName(source: List<ItemsGroup>?, searchFor: Int?): String {
        var result = ""
        if (source != null) {
            for (value in source) {
                if (searchFor == value.GroupCode) {
                    result = value.GroupName.toString()
                    break
                }
            }
        }
        return result
    }

    fun mapUomGroupCodeToName(source: List<UnitOfMeasurementGroups>?, searchFor: Int?): String {
        var result = ""
        if (source != null) {
            for (value in source) {
                if (searchFor == value.GroupCode) {
                    result = value.GroupName.toString()
                    break
                }
            }
        }
        return result
    }

    fun mapAllUomCodesToNames(
        sourceList: List<UoMGroupDefinitionCollection>?,
        lookFrom: List<UnitOfMeasurement>?,
    ): List<UnitOfMeasurement> {
        val resultList = ArrayList<UnitOfMeasurement>()
        if (sourceList != null && lookFrom != null) {

            for (uomInCollection in sourceList) {

                for (uom in lookFrom) {

                    if (uomInCollection.AlternateUoM == uom.UomCode) {
                        resultList.add(UnitOfMeasurement(uom.UomCode, uom.UomName))
                        break
                    }

                }

            }

        }
        return resultList
    }

    fun mapBpGroupCodeToName(source: List<BusinessPartnerGroups>?, searchFor: Int?): String {
        var result = ""
        if (source != null) {
            for (value in source) {
                if (searchFor == value.Code) {
                    result = value.Name.toString()
                    break
                }
            }
        }
        return result
    }

    fun mapPriceListCodeToName(source: List<PriceLists>?, searchFor: Int?): String {
        var result = ""
        if (source != null) {
            for (value in source) {
                if (searchFor == value.priceListNo) {
                    result = value.priceListName
                    break
                }
            }
        }
        return result
    }

    fun mapPricelistToPrice(sourceList: List<ItemPrices>?, searchFor: Int?): Double {
        var result = 0.0
        if (sourceList != null) {
            for (value in sourceList) {
                if (searchFor == value.priceList) {
                    result = value.price
                    break
                }
            }
        }
        return result

    }


    /*
    fun mapBpDebtByShop(sourceList: List<BusinessPartnersDebtForPost>): List<BusinessPartnersDebtByShop> {
        val resultList = ArrayList<BusinessPartnersDebtByShop>()
        resultList.add(
            BusinessPartnersDebtByShop(
                sourceList[0].whsCode,
                sourceList[0].docTotal!! - sourceList[0].paidToDate!!
            )
        )

        for (sourceItem in sourceList) {
            val whs = sourceItem.whsCode
            val debt = sourceItem.multiplyBy!! * (sourceItem.docTotal!! - sourceItem.paidToDate!!)

            if (resultList.isEmpty()) {
                resultList.add(BusinessPartnersDebtByShop(whs, debt))
                continue
            }

            for (resultItem in resultList) {
                if (resultItem.whsCode == whs) {
                    resultItem.debtByShop = resultItem.debtByShop?.plus(debt)
                } else {
                    resultList.add(BusinessPartnersDebtByShop(whs, debt))
                    break
                }
            }
        }
        return resultList
    }

    fun mapBpDebtByShop(
        sourceList: List<BusinessPartnersDebtForPost>,
        whsCode: String
    ): List<BusinessPartnersDebtByShop> {
        var result: BusinessPartnersDebtByShop? = null

        for (sourceItem in sourceList) {
            val whs = sourceItem.whsCode
            val debt = sourceItem.multiplyBy!! * (sourceItem.docTotal!! - sourceItem.paidToDate!!)

            if (whsCode == whs) {
                if (result == null) {
                    result = BusinessPartnersDebtByShop(whs, debt)
                    continue
                }
                result.debtByShop = result.debtByShop?.plus(debt)
            }
        }

        return when (result) {
            null -> emptyList<BusinessPartnersDebtByShop>()
            else -> listOf(result)
        }
    }
    */

}