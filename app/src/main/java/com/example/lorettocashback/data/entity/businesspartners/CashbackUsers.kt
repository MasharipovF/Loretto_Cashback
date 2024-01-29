package com.example.lorettocashback.data.entity.businesspartners

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CashbackUsersVal(
    @SerializedName("value")
    var value: List<CashbackUsers>,
)

data class CashbackUsers(

    @SerializedName("UserCode")
    val userCode: String?,

    @SerializedName("FullName")
    val fullName: String?,

    @SerializedName("Phone")
    val phone: String?,

    @SerializedName("Gender")
    val gender: String?,

    @SerializedName("DateOfBirth")
    val dateOfBirth: String?,

    @SerializedName("Password")
    val password: String?,

    @SerializedName("UserTypeCode")
    val userTypeCode: Int,

    @SerializedName("UserTypeName")
    val userTypeName: String?,

    @SerializedName("CurrentCashback")
    val currentCashback: Double,

    @SerializedName("GainedCashback")
    val gainedCashback: Double,

    @SerializedName("WithdrewCashback")
    val withdrewCashback: Double,

    @SerializedName("id__")
    val id__: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userCode)
        parcel.writeString(fullName)
        parcel.writeString(phone)
        parcel.writeString(gender)
        parcel.writeString(dateOfBirth)
        parcel.writeString(password)
        parcel.writeInt(userTypeCode)
        parcel.writeString(userTypeName)
        parcel.writeDouble(currentCashback)
        parcel.writeDouble(gainedCashback)
        parcel.writeDouble(withdrewCashback)
        parcel.writeInt(id__)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CashbackUsers> {
        override fun createFromParcel(parcel: Parcel): CashbackUsers {
            return CashbackUsers(parcel)
        }

        override fun newArray(size: Int): Array<CashbackUsers?> {
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
