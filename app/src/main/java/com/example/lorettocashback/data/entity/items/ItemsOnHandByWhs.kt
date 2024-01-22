package com.example.lorettocashback.data.entity.items


import com.google.gson.annotations.SerializedName

data class ItemsOnHandByWhsVal(
    @SerializedName("value")
    val value: List<ItemsOnHandByWhs>
)

data class ItemsOnHandByWhs(
    @SerializedName("BinLocationsActivated")
    val binLocationsActivated: Boolean? = false,
    @SerializedName("IsCommited")
    val isCommitted: Double? = 0.0,
    @SerializedName("ItemCode")
    val itemCode: String? = null,
    @SerializedName("ItemName")
    val itemName: String? = null,
    @SerializedName("OnHand")
    val onHand: Double? = 0.0,
    @SerializedName("WhsCode")
    val whsCode: String? = null,
    @SerializedName("WhsName")
    val whsName: String? = null,
    @SerializedName("BarCode")
    val barCodes: String? = "",
    @SerializedName("GroupDiscountPayFor")
    var DiscountPayFor: Double = 0.0,
    @SerializedName("GroupDiscountForFree")
    var DiscountForFree: Double = 0.0,
    @SerializedName("GroupDiscountUpTo")
    var DiscountUpTo: Double = 0.0,
    @SerializedName("SeparateInvoiceItem")
    var SeparateInvoiceItem: Boolean=false,
)