package com.example.lorettocashback.data.entity.reports

import com.google.gson.annotations.SerializedName


data class CashReportVal(
    @SerializedName("value")
    var value: List<CashReport>,
)

data class CashReport(
    @SerializedName("DocEntry")
    val DocEntry: Int,
    @SerializedName("DocNum")
    val DocNum: String,
    @SerializedName("CardCode")
    val CardCode: String,
    @SerializedName("CardName")
    val CardName: String? = null,
    @SerializedName("DocTotalUZS")
    val DocTotalUZS: Double,
    @SerializedName("DocDate")
    val DocDate: String,
    @SerializedName("ShopCode")
    val ShopCode: String,
    @SerializedName("DocType")
    val DocType: String,
    @SerializedName("ObjectType")
    val ObjectType: String,
    @SerializedName("PaidUZS")
    val PaidUZS: Double,
    @SerializedName("CashUZS")
    val CashUZS: Double,
    @SerializedName("Card")
    val Card: Double,
    @SerializedName("Epayment")
    val Epayment: Double,
    @SerializedName("ShopName")
    val ShopName: String? = null,
)
