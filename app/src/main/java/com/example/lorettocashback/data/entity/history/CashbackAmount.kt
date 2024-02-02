package com.example.lorettocashback.data.entity.history

import com.google.gson.annotations.SerializedName


data class CashbackAmountVal(
    @SerializedName("value")
    var value: List<CashbackAmount>,
)

data class CashbackAmount(

    @SerializedName("CashbackAmount")
    val cashbackAmount: Double,

    @SerializedName("OperationType")
    val operationType: String

)