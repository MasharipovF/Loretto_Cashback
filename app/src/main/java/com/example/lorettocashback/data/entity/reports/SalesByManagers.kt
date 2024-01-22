package com.example.lorettocashback.data.entity.reports


import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SalesByManagersVal(
    @SerializedName("value")
    val value: List<SalesByManagers>? = null
)

data class SalesByManagers(
    @SerializedName("ClosedSum")
    val ClosedSum: BigDecimal = BigDecimal.ZERO,
    @SerializedName("Returns")
    val Returns: BigDecimal = BigDecimal.ZERO,
    @SerializedName("Sales")
    val Sales: BigDecimal = BigDecimal.ZERO,
    @SerializedName("ShopCode")
    val ShopCode: String? = null,
    @SerializedName("ShopName")
    val ShopName: String? = null,
    @SerializedName("SlpName")
    val SlpName: String? = null
)