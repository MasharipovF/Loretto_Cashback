package com.example.lorettocashback.data.entity.reports


import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TopGoodsReportVal(
    @SerializedName("@odata.context")
    val odataContext: String? = null,
    @SerializedName("@odata.nextLink")
    val odataNextLink: String? = null,
    @SerializedName("value")
    val value: List<TopGoodsReport>? = null
)
data class TopGoodsReport(
    @SerializedName("id__")
    val id: Int? = null,
    @SerializedName("ItemCode")
    val itemCode: String? = null,
    @SerializedName("ItemName")
    val itemName: String? = null,
    @SerializedName("ItemsGroupCode")
    val itemsGroupCode: Int? = null,
    @SerializedName("Quantity")
    val quantity: BigDecimal = BigDecimal.ZERO,
    @SerializedName("OnHand")
    val onhand: BigDecimal = BigDecimal.ZERO,
    @SerializedName("Sum")
    val sum: BigDecimal = BigDecimal.ZERO,
)