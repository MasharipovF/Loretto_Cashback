package com.example.lorettocashback.data.entity.qr_code.request

import com.google.gson.annotations.SerializedName

data class CashbackTransRowCollection(

    @SerializedName("U_itemCode")
    val uItemCode: String?,

    @SerializedName("U_itemName")
    val uItemName: String?,

    @SerializedName("U_serialNumber")
    val uSerialNumber: String?,

    @SerializedName("U_asllik")
    val uAsllik: String?,

    @SerializedName("U_absEntry")
    val uAbsEntry: Int?,

    @SerializedName("U_cashbackAmount")
    val uCashbackAmount: Double,
)