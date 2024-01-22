package com.example.lorettocashback.data.entity.reports

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


data class SalesByShopVal(
    @SerializedName("value")
    var value: List<SalesByShop>,
)

data class SalesByShop(
    @SerializedName("ShopCode")
    val ShopCode: String? = null,
    @SerializedName("ShopName")
    val ShopName: String? = null,
    @SerializedName("Sales")
    val Sales: BigDecimal = BigDecimal(0),
    @SerializedName("MoneyIn")
    val MoneyIn: BigDecimal = BigDecimal(0),
    @SerializedName("MoneyOut")
    val MoneyOut: BigDecimal = BigDecimal(0),
    @SerializedName("Result")
    val Result: BigDecimal = BigDecimal(0)
)
