package com.example.lorettocashback.data.entity.qr_code.response

import com.google.gson.annotations.SerializedName

data class QrCodeResponse(
    @SerializedName("CASHBACK_TRANS_ROWCollection")
    val CASHBACK_TRANS_ROWCollection: List<CollectionResponse>,

    @SerializedName("Canceled")
    val canceled: String,

    @SerializedName("CreateDate")
    val createDate: String,

    @SerializedName("CreateTime")
    val createTime: String,

    @SerializedName("Creator")
    val creator: String,

    @SerializedName("DataSource")
    val dataSource: String,

    @SerializedName("DocEntry")
    val docEntry: Int,

    @SerializedName("DocNum")
    val docNum: Int,

    @SerializedName("Handwrtten")
    val handwrtten: String,

    @SerializedName("Instance")
    val instance: Int,

    @SerializedName("LogInst")
    val logInst: Any,

    @SerializedName("Object")
    val Object: String,

    @SerializedName("Period")
    val period: Int,

    @SerializedName("Remark")
    val remark: Any,

    @SerializedName("RequestStatus")
    val requestStatus: String,

    @SerializedName("Series")
    val series: Int,

    @SerializedName("Status")
    val status: String,

    @SerializedName("Transfered")
    val transfered: String,

    @SerializedName("U_date")
    val uDate: String,

    @SerializedName("U_operationType")
    val uOperationType: String,

    @SerializedName("U_total")
    val uTotal: Double,

    @SerializedName("U_userId")
    val uUserId: String,

    @SerializedName("UpdateDate")
    val updateDate: String,

    @SerializedName("UpdateTime")
    val updateTime: String,

    @SerializedName("UserSign")
    val userSign: Int
)