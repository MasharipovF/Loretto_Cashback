package com.example.lorettocashback.data.entity.documents

import android.os.Parcelable
import com.example.lorettocashback.core.GeneralConsts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DocumentsVal(
    @SerializedName("value")
    @Expose
    var value: List<Document>,

    @SerializedName("odata.nextLink")
    @Expose
    var nextlink: String
)

@Parcelize
data class Document(

    @SerializedName("DocEntry")
    @Expose
    var DocEntry: Long? = null,

    @SerializedName("DocNum")
    @Expose
    var DocNum: String? = null,

    @SerializedName("BPL_IDAssignedToInvoice")
    var BPL_ID: String? = null,

    @SerializedName("NumAtCard")
    @Expose
    var NumAtCard: String? = null,

    @SerializedName("DocDate")
    @Expose
    var DocDate: String? = null,

    @SerializedName("DocTime")
    @Expose
    var DocTime: String? = null,

    @SerializedName("DocDueDate")
    @Expose
    var DocDueDate: String? = null,

    @SerializedName("UpdateTime")
    @Expose
    var UpdateTime: String? = null,

    @SerializedName("UpdateDate")
    @Expose
    var UpdateDate: String? = null,

    @SerializedName("UpdateCount")
    @Expose
    var UpdateCount: Int = 0,

    @SerializedName("CardCode")
    @Expose
    var CardCode: String? = null,

    @SerializedName("CardName")
    @Expose
    var CardName: String? = null,

    @SerializedName("U_phone")
    @Expose
    var U_phone: String? = "",

    @SerializedName("DocumentLines")
    @Expose
    var DocumentLines: List<DocumentLines>,

    @SerializedName("DocTotal")
    @Expose
    var DocTotal: Double? = 0.0,

    @SerializedName("OpenSum")
    @Expose
    var OpenSum: Double? = 0.0,



    @SerializedName("U_sumUZS")
    @Expose
    var DocTotalUZS: Double? = 0.0,

    @SerializedName("U_sumUZSbefDisc")
    @Expose
    var DocTotalUZSBefDiscount: Double? = 0.0,

    @SerializedName("DocTotalSys")
    @Expose
    var DocTotalSys: Double? = 0.0,

    @SerializedName("PaidToDate")
    @Expose
    var PaidToDate: Double? = 0.0,

    @SerializedName("PaidToDateSys")
    @Expose
    var PaidToDateSys: Double? = 0.0,

    @SerializedName("DiscountPercent")
    @Expose
    var DiscountPercent: Double? = 0.0,

    @SerializedName("DocCurrency")
    @Expose
    var DocCurrency: String? = null,

    @SerializedName("Cancelled")
    @Expose
    var Cancelled: String? = null,

    @SerializedName("DocumentStatus")
    @Expose
    var DocumentStatus: String? = null,

    @SerializedName("DocObjectCode")
    var DocObjectCode: String? = null,

    @SerializedName("ShipToCode")
    var ShipToCode: String? = null,

    @SerializedName("U_whs")
    var WhsCode: String? = null,

    @SerializedName("Comments")
    var Comments: String? = null,

    @SerializedName("SalesPersonCode")
    var SalesManagerCode: Long? = null,

    @SerializedName("SalesPersonName")
    var SalesManagerName: String? = null,

    var SalesManagerPhone: String? = null,

    var isChecked: Boolean = false

): Parcelable

@Parcelize
data class DocumentLines(
    @SerializedName("ItemCode")
    @Expose
    var ItemCode: String? = null,

    @SerializedName("ItemDescription")
    @Expose
    var ItemName: String? = null,

    var OnHand: Double? = null,

    var Committed: Double? = null,

    var isOnHandValuesLoaded: Boolean = false,

    var SeparateInvoiceItem: Boolean = false,

    @SerializedName("U_bundleCode")
    var BundleCode: String? = null,

    @SerializedName("LineStatus")
    @Expose
    var LineStatus: String? = null,

    @SerializedName("Quantity")
    @Expose
    var Quantity: Double? = 0.0,

    @SerializedName("U_OINVDocEntry")
    @Expose
    var OINVDocEntry: Long? = null,

    @SerializedName("RemainingOpenQuantity")
    @Expose
    var RemainingOpenQuantity: Double? = null,

    var UserQuantity: Double? = null,

    var MaxQuantity: Double? = null,

    var InitialQuantity: Double = 0.0,

    var BaseQuantity: Double? = 0.0,

    @SerializedName("WarehouseCode")
    @Expose
    var WarehouseCode: String = "",

   /* @SerializedName("UnitPrice")
    @Expose
    var PriceUSD: Double? = null,*/

    @SerializedName("PriceAfterVAT")
    @Expose
    var PriceAfterVATUSD: Double? = 0.0,

    @SerializedName("U_priceUZS")
    @Expose
    var PriceAfterVATUZS: Double? = null,

    var UserPriceAfterVATUSD: Double? = null,


    var UserPriceAfterVATUZS: Double? = null,

    var BasePriceUZS: Double = 0.0,

    @SerializedName("UnitPrice")
    @Expose
    var BasePriceUSD: Double = 0.0,

    @SerializedName("DiscountPercent")
    @Expose
    var DiscountPercent: Double = 0.0,

    @SerializedName("U_discType")
    @Expose
    var DiscountType: String? = GeneralConsts.DISCOUNT_TYPE_NO,

    var DiscountBase: Double = 0.0,

    var DiscountByQuantity: Double = 0.0,

    var DiscountByQuantityLineNum: Int? = null,

    var DiscountPayFor: Double = 0.0,

    var DiscountForFree: Double = 0.0,

    var DiscountUpTo: Double = 0.0,

    @SerializedName("U_isFree")
    @Expose
    var IsFreeItem: Boolean = false,

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
    var BaseLine: Long? = null
): Parcelable
