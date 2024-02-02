package com.example.lorettocashback.data.entity.history

import com.google.gson.annotations.SerializedName

data class CashbackHistoryVal(
    @SerializedName("value")
    var value: List<CashbackHistory>,
)

data class CashbackHistory(

    @SerializedName("DocEntry")
    val docEntry: Int,

    @SerializedName("DocNum")
    val docNum: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("UserCode")
    val userCode: String,

    @SerializedName("UserName")
    val userName: String,

    @SerializedName("Cancelled")
    val cancelled: String,

    @SerializedName("OperationType")
    val operationType: String,

    @SerializedName("TotalAmount")
    val totalAmount: Double,

    @SerializedName("ItemCode")
    val itemCode: String,

    @SerializedName("ItemName")
    val itemName: String,

    @SerializedName("ItemsGroupCode")
    val itemsGroupCode: Int,

    @SerializedName("ItemsGroupName")
    val itemsGroupName: String,

    @SerializedName("SerialNumber")
    val serialNumber: String,

    @SerializedName("Asllik")
    val asllik: String,

    @SerializedName("AbsEntry")
    val absEntry: Int,

    @SerializedName("CashbackAmount")
    val cashbackAmount: Double,

    @SerializedName("id__")
    val id__: Int
)