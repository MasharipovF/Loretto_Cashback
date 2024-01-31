package com.example.lorettocashback.data.entity.qr_code

import com.google.gson.annotations.SerializedName

data class CashbackQrCodeVal(
    @SerializedName("value")
    var value: List<CashbackQrCode>,
)

data class CashbackQrCode(

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
    val asllik: Any,

    @SerializedName("AbsEntry")
    val absEntry: Int,

    @SerializedName("CashbackAmount")
    val cashbackAmount: Double,

    @SerializedName("Quantity")
    val quantity: Double,

    @SerializedName("id__")
    val id__: Int
)