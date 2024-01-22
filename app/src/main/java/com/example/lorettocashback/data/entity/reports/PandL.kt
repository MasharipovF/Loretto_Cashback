package com.example.lorettocashback.data.entity.reports


import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PandLVal(
    @SerializedName("@odata.context")
    val odataContext: String? = null,
    @SerializedName("value")
    val value: List<PandL>? = null
)
data class PandL(
    @SerializedName("ExpenseUSD")
    val expenseUSD: BigDecimal = BigDecimal.ZERO,
    @SerializedName("ExpenseUZS")
    val expenseUZS: BigDecimal = BigDecimal.ZERO,
    @SerializedName("id__")
    val id: Int? = null,
    @SerializedName("ResultUSD")
    val resultUSD: BigDecimal = BigDecimal.ZERO,
    @SerializedName("ResultUZS")
    val resultUZS: BigDecimal = BigDecimal.ZERO,
    @SerializedName("RevenueUSD")
    val revenueUSD: BigDecimal = BigDecimal.ZERO,
    @SerializedName("RevenueUZS")
    val revenueUZS: BigDecimal = BigDecimal.ZERO,
    @SerializedName("ShopCode")
    val shopCode: String? = null,
    @SerializedName("ShopName")
    val shopName: String? = null
)
