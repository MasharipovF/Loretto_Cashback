package com.example.lorettocashback.util

enum class ObjectTypes(val objectCode: Int, val objectName: String) {
    INVOICE(13, "Продажа"),
    RETURN(14, "Возврат"),
    INPAYMENT(24, "Оплата"),
    OUTPAYMENT(46, "Возврат денег"),
    BEGINNING_BALANCE(-100, "Сальдо на начало"),
    ENDING_BALANCE(-200, "Сальдо на конец"),
}
