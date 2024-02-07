package com.example.lorettocashback.data.entity.qr_code.request

import com.google.gson.annotations.SerializedName

data class QrCodeRequest(

    @SerializedName("CASHBACK_TRANS_ROWCollection")
    val CASHBACK_TRANS_ROWCollection: List<CashbackTransRowCollection>,

    @SerializedName("U_date")
    val uDate: String,

    @SerializedName("U_operationType")
    val uOperationType: String,

    @SerializedName("U_total")
    val uTotal: Double,

    @SerializedName("U_userId")
    val uUserId: String
)