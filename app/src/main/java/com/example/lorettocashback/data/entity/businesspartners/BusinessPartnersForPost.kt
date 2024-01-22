package com.example.lorettocashback.data.entity.businesspartners

import com.example.lorettocashback.core.GeneralConsts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class BusinessPartnersForPost(

    @SerializedName("CardName")
    @Expose
    var CardName: String? = null,

    @SerializedName("CardType")
    @Expose
    var CardType: String? = GeneralConsts.BP_TYPE_CUSTOMER,

    @SerializedName("GroupCode")
    @Expose
    var GroupCode: Int? = null,

    @SerializedName("Phone1")
    @Expose
    var Phone1: String? = null,

    @SerializedName("CreditLimit")
    @Expose
    var CreditLimit: Double? = 0.0,

    @SerializedName("PriceListNum")
    @Expose
    var DefaultPriceListCode: Int? = -1,

    @SerializedName("Series")
    @Expose
    var Series: Int? = null,

    @SerializedName("BPAddresses")
    @Expose
    var BPAddresses: List<BPAddresses>? = listOf(),

    @SerializedName("Currency")
    @Expose
    var Currency: String? = null,
)