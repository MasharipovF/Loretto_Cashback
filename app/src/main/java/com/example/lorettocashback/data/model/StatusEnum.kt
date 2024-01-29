package com.example.lorettocashback.data.model

import com.example.lorettocashback.R

enum class StatusEnum(val colorItemEnum: Int, val textColorEnum: String) {
    GAINED(R.drawable.bg_green, "#358E39"),
    RETURNED(R.drawable.bg_red, "#E53935"),
    WITHDREW(R.drawable.bg_blue, "#0F64AF"),
    CANCELLED(R.drawable.bg_orange, "#FF6F00")
}