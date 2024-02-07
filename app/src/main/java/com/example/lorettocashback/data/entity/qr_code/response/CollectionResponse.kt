package com.example.lorettocashback.data.entity.qr_code.response

import com.google.gson.annotations.SerializedName

data class CollectionResponse(
    @SerializedName("DocEntry")
    val docEntry: Int,

    @SerializedName("LineId")
    val lineId: Int,

    @SerializedName("LogInst")
    val logInst: Any,

    @SerializedName("Object")
    val Object: String,

    @SerializedName("U_absEntry")
    val uAbsEntry: Int,

    @SerializedName("U_asllik")
    val uAsllik: Any,

    @SerializedName("U_cashbackAmount")
    val uCashbackAmount: Double,

    @SerializedName("U_itemCode")
    val uItemCode: String,

    @SerializedName("U_itemName")
    val uItemName: String,

    @SerializedName("U_serialNumber")
    val uSerialNumber: String,

    @SerializedName("VisOrder")
    val visOrder: Int
)