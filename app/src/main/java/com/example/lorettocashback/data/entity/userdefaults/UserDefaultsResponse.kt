package com.example.lorettocashback.data.entity.userdefaults


import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.util.Transformable
import com.google.gson.annotations.SerializedName

data class UserDefaultsResponseVal(
    @SerializedName("@odata.context")
    val odataContext: String,
    @SerializedName("value")
    val value: List<UserDefaultsResponse>
) : Transformable<UserDefaults> {
    override fun transform(): UserDefaults {
        var branches: List<UserDefaultsBranches> = arrayListOf()
        branches =
            value.map { UserDefaultsBranches(branchId = it.branchId, branchName = it.branchName) }
        return UserDefaults(
            branches = branches,
            cardCode = value[0].cardCode,
            cardName = value[0].cardName,
            cashAccount = value[0].cashAccount,
            defBranchId = value[0].defBranchId,
            defBranchName = value[0].defBranchName,
            defTaxCode = value[0].defTaxCode,
            defaultsGroup = value[0].defaultsGroup,
            isMobileUser = value[0].isMobileUser == GeneralConsts.YES,
            isSuperUser = value[0].isSuperUser == GeneralConsts.YES,
            salesPersonCode = value[0].salesPersonCode,
            salesPersonName = value[0].salesPersonName,
            userCode = value[0].userCode,
            userId = value[0].userId,
            userName = value[0].userName,
            whsCode = value[0].whsCode,
            whsName = value[0].whsName,
            intermediaryWhs = value[0].intermediaryWhs,
            defectWhs = value[0].defectWhs,
            accountUZS = value[0].accountUZS,
            accountUSD = value[0].accountUSD,
            priceList = value[0].priceList,
            salesOrdersVisibleDays = value[0].salesOrdersVisibleDays?:10000,
            salesOrdersChangeLimit = value[0].salesOrdersChangeLimit?:10000,
            cashbackItemCode = value[0].cashbackItemCode,
            restrictDebtAfter = value[0].restrictDebtAfter,
            returnsPassword = value[0].returnsPassword
        )
    }
}

data class UserDefaultsResponse(
    @SerializedName("CardCode")
    val cardCode: String?,
    @SerializedName("CardName")
    val cardName: String?,
    @SerializedName("CashAccount")
    val cashAccount: String?,
    @SerializedName("DefBranchId")
    val defBranchId: Int?,
    @SerializedName("DefBranchName")
    val defBranchName: String?,
    @SerializedName("DefTaxCode")
    val defTaxCode: String?,
    @SerializedName("DefaultsGroup")
    val defaultsGroup: String?,
    @SerializedName("IsMobileUser")
    val isMobileUser: String,
    @SerializedName("IsSuperUser")
    val isSuperUser: String,
    @SerializedName("SalesPersonCode")
    val salesPersonCode: Int?,
    @SerializedName("SalesPersonName")
    val salesPersonName: String?,
    @SerializedName("UserCode")
    val userCode: String,
    @SerializedName("UserId")
    val userId: Int,
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("WhsCode")
    val whsCode: String?,
    @SerializedName("WhsName")
    val whsName: String?,
    @SerializedName("BranchId")
    val branchId: Int?,
    @SerializedName("BranchName")
    val branchName: String?,
    @SerializedName("IntermediaryWhs")
    val intermediaryWhs: String?,
    @SerializedName("DefectWhs")
    val defectWhs: String?,
    @SerializedName("SalesOrdersVisibleDays")
    val salesOrdersVisibleDays: Int?,
    @SerializedName("SalesOrdersChangeLimit")
    val salesOrdersChangeLimit: Int?,
    @SerializedName("CashBackItemCode")
    val cashbackItemCode: String?,
    @SerializedName("RestrictDebtAfter")
    val restrictDebtAfter: Int?,
    @SerializedName("AccountUZS")
    val accountUZS: String?,
    @SerializedName("AccountUSD")
    val accountUSD: String?,
    @SerializedName("ReturnsPassword")
    val returnsPassword: String?,
    @SerializedName("PriceList")
    val priceList: Int?

)
