package com.example.lorettocashback.data.entity.businesspartners

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BusinessPartnersVal(
    @SerializedName("value")
    var value: List<BusinessPartners>,

    @SerializedName("odata.nextLink")
    var nextlink: String

)

data class BusinessPartners(
    @SerializedName("CardCode")
    var CardCode: String? = null,

    @SerializedName("CardName")
    var CardName: String? = null,

    @SerializedName("CardType")
    var CardType: String? = null,

    @SerializedName("Currency")
    var Currency: String? = null,

    @SerializedName("GroupCode")
    var GroupCode: Int? = 0,

    @SerializedName("GroupName")
    var GroupName: String? = null,

    @SerializedName("DueDay")
    var DueDay: Int = 0,


    @SerializedName("CurrentAccountBalance")
    var Balance: Double = 0.0,

    @SerializedName("CurrentAccountBalanceByShop")
    var BalanceByShop: Double? = 0.0,

    @SerializedName("TotalCashBack")
    var TotalCashBack: Double? = 0.0,

    @SerializedName("CashBackWithdrew")
    var CashBackWithdrew: Double? = 0.0,

    @SerializedName("cashback")
    var CurrentCashBack: Double? = 0.0,

    @SerializedName("OpenOrdersBalance")
    var OpenOrdersBalance: Double? = null,

    @SerializedName("Phone1")
    var Phone1: String? = null,

    @SerializedName("ShipToDefault")
    var Address: String? = null,

    @SerializedName("CreditLimit")
    var CreditLimit: Double = 0.0,

    @SerializedName("MaxCommitment")
    var MaxCommitment: Double? = 0.0,

    @SerializedName("PriceListNum")
    var PriceListCode: Int? = -1,

    @SerializedName("PriceListName")
    var PriceListName: String? = null,

    @SerializedName("Valid")
    var Valid: String? = "tYES",

    @SerializedName("Frozen")
    var Frozen: String? = "tNO",

    @SerializedName("Series")
    var Series: Int? = null,
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(CardCode)
        parcel.writeString(CardName)
        parcel.writeString(CardType)
        parcel.writeString(Currency)
        parcel.writeValue(GroupCode)
        parcel.writeString(GroupName)
        parcel.writeInt(DueDay)
        parcel.writeDouble(Balance)
        parcel.writeValue(BalanceByShop)
        parcel.writeValue(TotalCashBack)
        parcel.writeValue(CashBackWithdrew)
        parcel.writeValue(CurrentCashBack)
        parcel.writeValue(OpenOrdersBalance)
        parcel.writeString(Phone1)
        parcel.writeString(Address)
        parcel.writeDouble(CreditLimit)
        parcel.writeValue(MaxCommitment)
        parcel.writeValue(PriceListCode)
        parcel.writeString(PriceListName)
        parcel.writeString(Valid)
        parcel.writeString(Frozen)
        parcel.writeValue(Series)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BusinessPartners> {
        override fun createFromParcel(parcel: Parcel): BusinessPartners {
            return BusinessPartners(parcel)
        }

        override fun newArray(size: Int): Array<BusinessPartners?> {
            return arrayOfNulls(size)
        }
    }

}


data class ContactPersons(
    @SerializedName("Active")
    var active: String = "",

    @SerializedName("Address")
    var address: Any? = null,

    @SerializedName("CardCode")
    var cardCode: String = "",

    @SerializedName("E_Mail")
    var eMail: Any? = null,

    @SerializedName("FirstName")
    var firstName: String = "",

    @SerializedName("InternalCode")
    var internalCode: Int = 0,

    @SerializedName("LastName")
    var lastName: Any? = null,

    @SerializedName("MiddleName")
    var middleName: Any? = null,

    @SerializedName("MobilePhone")
    var mobilePhone: Any? = null,

    @SerializedName("Name")
    var name: String = "",

    @SerializedName("Phone1")
    var phone1: String = "",

    @SerializedName("Phone2")
    var phone2: Any? = null,

    @SerializedName("Position")
    var position: Any? = null
)
