package com.example.lorettocashback.data.entity.documents

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DocumentsForPostVal(
    @SerializedName("value")
    var value: List<Document>,

)

data class DocumentForPost(

    @SerializedName("DocEntry")
    var DocEntry: Long? = null,

    @SerializedName("DocNum")
    var DocNum: String? = null,

    @SerializedName("BPL_IDAssignedToInvoice")
    var BPL_ID: String? = null,

    @SerializedName("NumAtCard")
    var NumAtCard: String? = null,

    @SerializedName("DocDate")
    var DocDate: String? = null,

    @SerializedName("DocDueDate")
    var DocDueDate: String? = null,

    @SerializedName("CardCode")
    @Expose
    var CardCode: String? = null,

    @SerializedName("CardName")
    @Expose
    var CardName: String? = null,

    @SerializedName("U_phone")
    @Expose
    var U_phone: String? = null,

    @SerializedName("DocumentLines")
    @Expose
    var DocumentLines: List<DocumentLinesForPost>,

    @SerializedName("DocTotal")
    @Expose
    var DocTotal: Double? = null,

    @SerializedName("U_sumUZS")
    @Expose
    var DocTotalUZS: Double? = 0.0,

    @SerializedName("U_sumUZSbefDisc")
    @Expose
    var DocTotalUZSBefDiscount: Double? = 0.0,

    @SerializedName("U_cash")
    var uCash: Double? =0.0,

    @SerializedName("U_card")
    var uCard: Double? =0.0,

    @SerializedName("U_ePayment")
    var uePayment: Double?=0.0,

    @SerializedName("U_totalPaidSum")
    var uTotalPaidSum: Double?=0.0,

    @SerializedName("DiscountPercent")
    @Expose
    var DiscountPercent: Double? = null,

    @SerializedName("DocCurrency")
    @Expose
    var DocCurrency: String? = null,

    @SerializedName("Cancelled")
    @Expose
    var Cancelled: String? = null,

    @SerializedName("ShipToCode")
    var ShipToCode: String = "",

    @SerializedName("U_whs")
    var WhsCode: String? = null,

    @SerializedName("Comments")
    var Comments: String? = null,

    @SerializedName("SalesPersonCode")
    var SalesManagerCode: Long? = null,

    @SerializedName("U_mobileAppId")
    var MobileAppId: String? = null,

    @SerializedName("U_cashback")
    var CashBack: Double?=0.0,
)

data class DocumentLinesForPost(
    @SerializedName("ItemCode")
    @Expose
    var ItemCode: String? = null,

    @SerializedName("ItemDescription")
    @Expose
    var ItemName: String? = null,

    @SerializedName("Quantity")
    @Expose
    var Quantity: Double? = null,

    @SerializedName("U_OINVDocEntry")
    @Expose
    var OINVDocEntry: Long? = null,

    @SerializedName("WarehouseCode")
    @Expose
    var WarehouseCode: String = "",

    @SerializedName("PriceAfterVAT")
    @Expose
    var PriceAfterVAT: Double? = null,

    @SerializedName("U_priceUZS")
    @Expose
    var UserPriceAfterVATUZS: Double? = null,


    @SerializedName("UnitPrice")
    @Expose
    var UnitPrice: Double? = null,

    @SerializedName("DiscountPercent")
    @Expose
    var DiscountPercent: Double? = null,

    @SerializedName("PriceSource")
    @Expose
    val PriceSource: String = "dpsManual",

    @SerializedName("LineNum")
    @Expose
    var LineNum: Long? = null,

    @SerializedName("BaseEntry")
    @Expose
    var BaseEntry: Long? = null,

    @SerializedName("BaseType")
    @Expose
    var BaseType: String? = null,

    @SerializedName("BaseLine")
    @Expose
    var BaseLine: Long? = null,

    @SerializedName("U_discType")
    @Expose
    var DiscountType: String? = null,

    @SerializedName("U_isFree")
    @Expose
    var IsFreeItem: Boolean = false,

    @SerializedName("U_bundleCode")
    @Expose
    var BundleCode: String? = null,

    )