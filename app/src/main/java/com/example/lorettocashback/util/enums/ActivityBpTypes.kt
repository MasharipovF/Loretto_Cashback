package com.example.lorettocashback.util.enums

enum class ActivityBpTypes(val typeCode: String, val daysAfter: Int?, val monthsAfter: Int?) {
    B2C("B2C", 2, null),
    B2B("B2B", 10, null),
    B2B_1("B2B", null, 1),
    B2B_2("B2B", null, 2),
    B2B_3("B2B", null, 3)
}