package com.example.lorettocashback.data.entity.userdefaults


data class UserDefaults(
    val branches: List<UserDefaultsBranches>?,
    val cardCode: String?,
    val cardName: String?,
    val cashAccount: String?,
    val defBranchId: Int?,
    val defBranchName: String?,
    val defTaxCode: String?,
    val defaultsGroup: String?,
    val isMobileUser: Boolean,
    val isSuperUser: Boolean,
    val salesPersonCode: Int?,
    val salesPersonName: String?,
    val userCode: String,
    val userId: Int,
    val userName: String,
    val whsCode: String?,
    val whsName: String?,
    val intermediaryWhs: String?,
    val defectWhs: String?,
    val salesOrdersVisibleDays: Int,
    val salesOrdersChangeLimit: Int,
    val cashbackItemCode: String?,
    val restrictDebtAfter: Int?,
    val accountUZS: String?,
    val accountUSD: String?,
    val returnsPassword: String?,
    val priceList: Int?

)
data class UserDefaultsBranches(
    var branchId: Int?,
    var branchName: String?
)
