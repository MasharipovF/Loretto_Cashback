package com.example.lorettocashback.domain.dto.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("CompanyDB")
    var companyDB: String? = null,

    @SerializedName("Password")
    var password: String? = null,

    @SerializedName("UserName")
    var userName: String? = null,

    @SerializedName("Language")
    var language: Int? = 24
)