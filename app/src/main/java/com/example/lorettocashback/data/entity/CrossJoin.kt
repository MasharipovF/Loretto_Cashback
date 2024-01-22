package com.example.lorettocashback.data.entity


import com.google.gson.annotations.SerializedName

data class CrossJoin(
    @SerializedName("QueryOption")
    var queryOption: String = "",
    @SerializedName("QueryPath")
    var queryPath: String = ""
)