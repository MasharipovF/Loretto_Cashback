package com.example.lorettocashback.util.enums

import com.example.lorettocashback.R

enum class ActivityStatus(val statusCode: String, val statusName: String, val statusBgColor: Int, val statusTextColor: Int) {
    NONE("N", "Status yoq", R.color.view_border, R.color.primaryTextCreditNoteColor),
    FAIL("F", "Muvaffaqiyatsiz", R.color.red_opacity_50, R.color.primaryColor),
    SUCCESS("S", "Muvaffaqiyatli", R.color.green_opacity_50, R.color.secondaryDarkColor),
    REPEATED_SALE("RS", "Qayta sotuv", R.color.blue_opacity_30, R.color.blue),
    DEFECT_ITEM("BR", "Brak tovar", R.color.orange_opacity_30, R.color.orange)
}