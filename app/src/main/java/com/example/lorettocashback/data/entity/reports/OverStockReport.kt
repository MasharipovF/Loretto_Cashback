package com.example.lorettocashback.data.entity.reports


import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class OverStockReportVal(
    @SerializedName("@odata.context")
    val odataContext: String? = null,
    @SerializedName("@odata.nextLink")
    val odataNextLink: String? = null,
    @SerializedName("value")
    val value: List<OverStockReport>? = null
)
data class OverStockReport(
    @SerializedName("Brand")
    val brand: String? = null,
    @SerializedName("DailyAvgSales")
    val dailyAvgSales: BigDecimal = BigDecimal.ZERO,
    @SerializedName("6daysStock")
    val sixDaysStock: BigDecimal = BigDecimal.ZERO,
    @SerializedName("30daysStock")
    val thirtyDaysStock: BigDecimal = BigDecimal.ZERO,
    @SerializedName("EnoughForDays")
    val enoughForDays: BigDecimal = BigDecimal.ZERO,
    @SerializedName("Group")
    val group: String? = null,
    @SerializedName("id__")
    val id: Int? = null,
    @SerializedName("ItemCode")
    val itemCode: String? = null,
    @SerializedName("ItemName")
    val itemName: String? = null,
    @SerializedName("OnHand")
    val onHand: BigDecimal = BigDecimal.ZERO,
    @SerializedName("OnWay")
    val onWay: BigDecimal = BigDecimal.ZERO,
    @SerializedName("OverstockQty")
    val overstockQty: BigDecimal = BigDecimal.ZERO,
    @SerializedName("OverstockSum")
    val overstockSum: BigDecimal = BigDecimal.ZERO,
    @SerializedName("Period")
    val period: Int? = null,
    @SerializedName("RecommendedStock")
    val recommendedStock: BigDecimal = BigDecimal.ZERO,
    @SerializedName("RequiredDaysStock")
    val requiredDaysStock: BigDecimal = BigDecimal.ZERO,
    @SerializedName("ShareInGroup")
    val shareInGroup: BigDecimal = BigDecimal.ZERO,
    @SerializedName("SoldQuantity")
    val soldQuantity: BigDecimal = BigDecimal.ZERO,
    @SerializedName("StockTillEndOfMonth")
    val stockTillEndOfMonth: BigDecimal = BigDecimal.ZERO,
    @SerializedName("SubGroup")
    val subGroup: String? = null
)
