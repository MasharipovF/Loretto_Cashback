package com.example.lorettocashback.data.entity.masterdatas


import com.google.gson.annotations.SerializedName

data class AppVersionVal(
    @SerializedName("value")
    val value: List<AppVersion>
)

data class AppVersion(
    @SerializedName("Name")
    val version: String?,
    @SerializedName("U_fromDate")
    val uFromDate: String,
    @SerializedName("U_link")
    val uLink: String?
)