package com.example.lorettocashback.core

object GeneralConsts {
    const val APP_VERSION = "1.0.1"

    const val COMPANY_DB="LORETTO"
    const val LOGIN="Support"
    const val PASSWORD="1234"

    const val EMPTY_STRING = ""
    const val RETURN_ALL_DATA = "odata.maxpagesize=99999999"

    const val TIMER_MS_IN_FUTURE: Long = 700L
    const val TIMER_INTERVAL: Long = 3000L


    const val PRINTER_TIMEOUT = 5000
    const val PRINTER_CHARSET = "Cp866"
    const val PRINTER_CHARSET_CODE = 17

    const val MAX_PAGE_SIZE = 20
    //TODO need to define all this at first start up, and move necessary to Preferences

    const val DOCUMENT_NOT_INSERTED = -1L
    const val DOCUMENT_INSERTED_AND_PAID = -2L

    const val PRIMARY_CURRENCY = "USD"
    const val SECONDARY_CURRENCY = "UZS"

    const val T_YES = "tYES"
    const val T_NO = "tNO"
    const val YES = "Y"
    const val NO = "N"




    const val BP_TYPE_CUSTOMER = "cCustomer"
    const val BP_TYPE_CUSTOMER_SML = "C"
    const val BP_TYPE_SUPPLIER = "cSupplier"
    const val BP_TYPE_SUPPLIER_SML = "S"

    const val DOC_STATUS_OPEN = "bost_Open"
    const val DOC_STATUS_CLOSED = "bost_Close"

    const val DOC_STATUS_OPEN_NAME = "Открыт"
    const val DOC_STATUS_CLOSED_NAME = "Закрыт"



    const val INTENT_EXTRA_VIEW_MODE="INTENT_EXTRA_VIEW_MODE"
    const val INTENT_EXTRA_ACTIVITY="INTENT_EXTRA_ACTIVITY"
    const val INTENT_EXTRA_BPTYPE="INTENT_EXTRA_BPTYPE"
    const val INTENT_EXTRA_DAYS_AFTER="INTENT_EXTRA_DAYS_AFTER"
    const val INTENT_EXTRA_MONTHS_AFTER="INTENT_EXTRA_MONTHS_AFTER"
    const val INTENT_EXTRA_STATUS="INTENT_EXTRA_STATUS"
    const val INTENT_EXTRA_BUSINESS_PARTNERS="INTENT_EXTRA_BUSINESS_PARTNERS"

    const val DISCOUNT_TYPE_NO = "N"
    const val DISCOUNT_TYPE_PRICE_CHANGED = "CP"
    const val DISCOUNT_TYPE_VOLUME_PERIOD = "V"
    const val DISCOUNT_TYPE_DOC_TOTAL = "T"
    const val DISCOUNT_TYPE_GROUP = "G"
    const val DISCOUNT_TYPE_GROUP_FREE_ITEMS = "GQ"
    const val DISCOUNT_TYPE_BUNDLE = "B"
    const val DISCOUNT_TYPE_SPECIAL_PRICES = "S"
    const val DISCOUNT_TYPE_PROHIBIT_DISCOUNT = "X"
}