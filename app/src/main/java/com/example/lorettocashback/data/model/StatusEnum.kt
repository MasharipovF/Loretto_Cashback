package com.example.lorettocashback.data.model

import com.example.lorettocashback.R

enum class StatusEnum(val statusName: String, val colorItemEnum: Int, val textColorEnum: Int) {
    GAINED("GAIN", R.drawable.bg_green, R.color.green_text),
    RETURNED("RETURN", R.drawable.bg_red, R.color.red_text),
    WITHDRAW("WITHDRAW", R.drawable.bg_blue, R.color.blue_text),
    CANCELLED("CANCEL", R.drawable.bg_orange, R.color.orange_text)
}