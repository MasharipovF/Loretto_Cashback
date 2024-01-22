package com.example.lorettocashback.data.entity.reports


import com.google.gson.annotations.SerializedName

data class NonLiquidGoodsVal(
    @SerializedName("@odata.context")
    val odataContext: String? = null,
    @SerializedName("@odata.nextLink")
    val odataNextLink: String? = null,
    @SerializedName("value")
    val value: List<NonLiquidGoods>? = null
)

data class NonLiquidGoods(
    @SerializedName("Brand")
    val brand: String? = null,
    @SerializedName("Group")
    val group: String? = null,
    @SerializedName("id__")
    val id: Int? = null,
    @SerializedName("ItemCode")
    val itemCode: String? = null,
    @SerializedName("ItemName")
    val itemName: String? = null,
    @SerializedName("LastInDate")
    val lastInDate: String? = null,
    @SerializedName("LastMoveDate")
    val lastMoveDate: String? = null,
    @SerializedName("LastOutDate")
    val lastOutDate: String? = null,
    @SerializedName("NoMoveDays")
    val noMoveDays: Int? = null,
    @SerializedName("StockAtLastMoveDate")
    val stockAtLastMoveDate: Double? = null,
    @SerializedName("SubGroup")
    val subGroup: String? = null,
    @SerializedName("Value")
    val value: Double? = null
)
