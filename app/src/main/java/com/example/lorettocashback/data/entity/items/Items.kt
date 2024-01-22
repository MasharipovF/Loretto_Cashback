package com.example.lorettocashback.data.entity.items

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class ItemsVal(
    @SerializedName("value")
    @Expose
    var items: List<Items>
)

data class Items(
    @SerializedName("ItemCode")
    @Expose
    var ItemCode: String? = null,

    @SerializedName("ItemName")
    @Expose
    var ItemName: String? = null,

    @SerializedName("ItemType")
    @Expose
    var ItemType: String? = "itItems",

    @SerializedName("ForeignName")
    @Expose
    var ForeignName: String? = null,

    @SerializedName("BarCode")
    @Expose
    var BarCode: String? = null,

    @SerializedName("ItemsGroupCode")
    @Expose
    var ItemsGroupCode: Int? = -1,

    var ItemsGroupName: String? = null,

    @SerializedName("SalesUnit")
    @Expose
    var SalesUnit: String? = null,

    @SerializedName("PurchaseUnit")
    @Expose
    var PurchaseUnit: String? = null,

    @SerializedName("InventoryUOM")
    @Expose
    var InventoryUOM: String? = null,

    @SerializedName("SeparateInvoiceItem")
    var SeparateInvoiceItem: Boolean=false,

    @SerializedName("QuantityOnStock")
    @Expose
    var TotalOnHand: Double = 0.0,

    @SerializedName("QuantityOnStockByCurrentWhs")
    @Expose
    var OnHandCurrentWhs: Double = 0.0,

    @SerializedName("CommittedByCurrentWhs")
    @Expose
    var CommittedCurrentWhs: Double = 0.0,

    @SerializedName("DiscountApplied")
    @Expose
    var DiscountApplied: Double = 0.0,

    @SerializedName("DiscountType")
    @Expose
    var DiscountType: String? = "N",

    @SerializedName("DiscountLineNum")
    @Expose
    var DiscountLineNum: Int = 0,

    @SerializedName("GroupDiscountPayFor")
    @Expose
    var DiscountPayFor: Double = 0.0,

    @SerializedName("GroupDiscountForFree")
    @Expose
    var DiscountForFree: Double = 0.0,

    @SerializedName("GroupDiscountUpTo")
    @Expose
    var DiscountUpTo: Double = 0.0,

    @SerializedName("Price")
    @Expose
    var Price: Double = 0.0,

    var DiscountedPrice: Double = 0.0,

    @SerializedName("Series")
    @Expose
    var Series: Int? = null,

    @SerializedName("Valid")
    @Expose
    var Valid: String? = "tYES",

    @SerializedName("Frozen")
    @Expose
    var Frozen: String? = "tNO",

    @SerializedName("UoMGroupEntry")
    @Expose
    var UoMGroupEntry: Int? = -1,

    var UoMGroupName: String? = null,

    @SerializedName("InventoryUoMEntry")
    @Expose
    var InventoryUoMEntry: Int? = -1,

    @SerializedName("DefaultSalesUoMEntry")
    @Expose
    var SalesUoMEntry: Int? = -1,

    @SerializedName("DefaultPurchasingUoMEntry")
    @Expose
    var PurchasingUoMEntry: Int? = -1,

    @SerializedName("ItemBarCodeCollection")
    @Expose
    var ItemBarCodeCollection: List<ItemBarCodes> = listOf(),

    @SerializedName("ItemWarehouseInfoCollection")
    @Expose
    var ItemWarehouseInfoCollection: List<ItemWarehouseInfo> = listOf(),

    @SerializedName("ItemPrices")
    @Expose
    var ItemPrices: List<ItemPrices> = listOf()
)

data class ItemBarCodes(
    @SerializedName("AbsEntry")
    @Expose
    var AbsEntry: String? = null,

    @SerializedName("UoMEntry")
    @Expose
    var UoMEntry: String? = null,

    @SerializedName("Barcode")
    @Expose
    var Barcode: String? = null,

    @SerializedName("FreeText")
    @Expose
    var FreeText: String? = null
)

data class ItemWarehouseInfo(
    @SerializedName("WarehouseCode")
    @Expose
    var WarehouseCode: String? = null,

    var WarehouseName: String? = null,

    @SerializedName("InStock")
    @Expose
    var InStock: Double = 0.0,

    @SerializedName("Committed")
    @Expose
    var Committed: Double = 0.0

)

data class ItemPrices(
    @SerializedName("AdditionalCurrency1")
    var additionalCurrency1: Any? = null,
    @SerializedName("AdditionalCurrency2")
    var additionalCurrency2: Any? = null,
    @SerializedName("AdditionalPrice1")
    var additionalPrice1: Double = 0.0,
    @SerializedName("AdditionalPrice2")
    var additionalPrice2: Double = 0.0,
    @SerializedName("BasePriceList")
    var basePriceList: Int = 0,
    @SerializedName("Currency")
    var currency: Any? = null,
    @SerializedName("Factor")
    var factor: Double = 0.0,
    @SerializedName("Price")
    var price: Double = 0.0,
    @SerializedName("PriceList")
    var priceList: Int = 0,
    @SerializedName("UoMPrices")
    var uoMPrices: List<Any> = listOf()
)