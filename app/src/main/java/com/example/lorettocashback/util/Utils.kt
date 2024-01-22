package com.example.lorettocashback.util

import android.app.Activity
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.lorettocashback.core.GeneralConsts.DOC_STATUS_CLOSED
import com.example.lorettocashback.core.GeneralConsts.DOC_STATUS_CLOSED_NAME
import com.example.lorettocashback.core.GeneralConsts.DOC_STATUS_OPEN
import com.example.lorettocashback.core.GeneralConsts.DOC_STATUS_OPEN_NAME
import com.example.lorettocashback.data.Preferences
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt


object Utils {

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager: InputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        } catch (e: Exception) {
        }
    }

    fun isSoftKeyboardVisible(view: View): Boolean {
        val heightDiff = view.rootView.height - view.height
        return heightDiff > 200.px
    }

    fun convertUSAdatetoNormal(date: String?): String {
        if (date == null) return ""

        val mDate = if (date.length > 10) date.substring(0, 10) else date


        val year: String = mDate.substringBefore("-")
        var day = mDate.substringAfterLast("-")
        if (day.length == 1) day = "0$day"
        var month = mDate.substringAfter("-").substringBefore("-")
        if (month.length == 1) month = "0$month"

        return "$day.$month.$year"
    }

    fun getDocStatus(statusCode: String): String {
        return when (statusCode) {
            DOC_STATUS_OPEN -> DOC_STATUS_OPEN_NAME
            DOC_STATUS_CLOSED -> DOC_STATUS_CLOSED_NAME
            else -> "???"
        }
    }

    fun getObjectCode(objectName: String): String? {
        return when (objectName) {
            "oOrders" -> "17"
            "1250000001" -> "InventoryTransferRequest"
            else -> null
        }
    }

    fun getDateInUSAFormat(year: String, sourceMonth: String, sourceDay: String): String {
        val day = if (sourceDay.length == 1) "0$sourceDay" else sourceDay
        val month = if (sourceMonth.length == 1) "0$sourceMonth" else sourceMonth
        return "$year-$month-$day"
    }

    fun getCurrentDateinUSAFormat(): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val mDay = if (day < 10) "0$day" else day
        val mMonth = if (month + 1 < 10) "0${month + 1}" else month + 1

        return "$year-${mMonth}-$mDay"
    }

    fun addDays(date: String?, daysToAdd: Int): String {
        val mDate = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        mDate.time = sdf.parse(date)

        Log.wtf("DATEEE", mDate.toString())

        mDate.add(Calendar.DAY_OF_MONTH, daysToAdd)

        Log.wtf("DATEEE_A", mDate.toString())

        val year = mDate.get(Calendar.YEAR)
        val month = mDate.get(Calendar.MONTH)
        val day = mDate.get(Calendar.DAY_OF_MONTH)

        val mDay = if (day < 10) "0$day" else day
        val mMonth = if (month + 1 < 10) "0${month + 1}" else month + 1

        return "$year-${mMonth}-$mDay"
    }

    fun addMonths(date: String?, monthToAdd: Int): String {
        val mDate = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        mDate.time = sdf.parse(date)

        Log.wtf("DATEEE", mDate.toString())

        mDate.add(Calendar.MONTH, monthToAdd)

        Log.wtf("DATEEE_A", mDate.toString())

        val year = mDate.get(Calendar.YEAR)
        val month = mDate.get(Calendar.MONTH)
        val day = mDate.get(Calendar.DAY_OF_MONTH)

        val mDay = if (day < 10) "0$day" else day
        val mMonth = if (month + 1 < 10) "0${month + 1}" else month + 1

        return "$year-${mMonth}-$mDay"

    }


    fun getCurrentDay(): Int {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)


        return day
    }

    fun getFirstDayOFCurrentMONTHinUSAFormat(): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        return "$year-${month + 1}-01"
    }

    fun getFirstDayOFCurrentYEARinUSAFormat(): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        return "$year-01-01"
    }


    fun getNumberWithThousandSeparator(number: Double?): String {
        if (number == null) return ""
        val separator = DecimalFormatSymbols()
        separator.groupingSeparator = ' '
        return if ((number == floor(number))) {
            //Check if value is integer
            DecimalFormat("#,##0", separator).format(number.toInt())
            //number.toInt().toString()
        } else {
            DecimalFormat("#,##0.00", separator).format(number)
        }
        // return String.format("%,.2f", roundDoubleValue(number, 2)).replace(",", " ")
    }

   /* fun getNumberString(number: Double): String {
        return String.format("%,d", number.roundToInt()).replace(",", "")

        /* return if ((number == floor(number))) {                    //Check if value is integer
         } else {
             String.format("%,.2f", number).replace(",", " ")
         }*/

    }*/

    fun getIntOrDoubleNumberString(value: Double, roundUntil: Int = 1): String {
        return if ((value == floor(value))) {                    //Check if value is integer
            value.toInt().toString()
        } else {
            roundDoubleValue(value, roundUntil).toString()
        }
    }


    fun getIntNumberString(value: Double?): String {
        if (value == null) return ""
        return value.roundToInt().toString()
    }

    fun roundDoubleValue(number: Double, roundUntil: Int = 2): Double {
        var pattern: String = "#."
        for (i in 1..roundUntil) {
            pattern += "#"
        }

        val df = DecimalFormat(pattern, DecimalFormatSymbols.getInstance(Locale.US))
        df.roundingMode = RoundingMode.HALF_UP
        return df.format(number).toDouble()
    }

    fun generateEan13Barcode(lastBarcode: String): String {
        var result = ""
        var checkDigit = 0
        val defaultBarcode = "2000000000008"
        var evenSum = 0;
        var oddSum = 0;

        if (lastBarcode.isEmpty()) return defaultBarcode

        result = lastBarcode.substring(
            0,
            lastBarcode.length - 2
        ) + (lastBarcode[lastBarcode.length - 2] + 1)

        result.forEachIndexed { index: Int, char: Char ->
            //if (index % 2 == 0) oddSum += char.toInt()
            //else evenSum += char.toInt() * 3
        }


        for (i in 0..11) {

            val currentValue = Character.getNumericValue(result.get(i))
            if (i % 2 == 0) oddSum += currentValue
            else evenSum += currentValue * 3

        }

        checkDigit = Character.getNumericValue((evenSum + oddSum).toString().last())

        result = if (checkDigit == 0) "${result}0"
        else "${result}${10 - checkDigit}"

        return result

    }


    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


}