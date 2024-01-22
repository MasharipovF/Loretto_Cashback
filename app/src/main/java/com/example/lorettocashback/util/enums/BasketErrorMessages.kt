package com.example.lorettocashback.util.enums

enum class BasketErrorMessages(val message: String) {
    NEGATIVE_QUANTITY("Остаткада товар етмаяпти!"),
    LESS_THAN_ZERO("Товар сони 0 дан кичик булиши мумкин емас!"),
    SEPARATE_INVOICE("Бу товарни алохида чекда уриш керак!"),
}