package com.example.lorettocashback.util.xprinter

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.example.lorettocashback.R
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.documents.Document
import com.example.lorettocashback.util.Utils

object PrinterHelper {

    fun printReceipt(
        printer: EscPosPrinter,
        applicationContext: Context,
        document: Document,
        phone: String,
        managerName: String,
        managerMobile: String?,
        paidSum: Double,
        isReturn: Boolean,
    ): Boolean {

        Log.wtf("PRINTERHELPER", document.toString())

        val docType = if (isReturn) "ВОЗВРАТ" else "ПРОДАЖА"
        val seconCut = Preferences.printerMaxChar - 18

        var docTotalWithoutDiscountUZS: Double = 0.0

        val imageResource = R.drawable.printerlogo


        try {
            var docLinesText = ""
            // formatted document lines
            document.DocumentLines.forEachIndexed { index, documentLines ->
                val realIndex = index + 1
                val indexOffset =
                    if (realIndex > 999) "$realIndex " else if (realIndex in 100..999) "$realIndex  " else if (realIndex in 10..99) "$realIndex   " else "$realIndex    "

                val rowAndItemName = "${indexOffset}${documentLines.ItemName}"
                var tempstring = rowAndItemName
                Log.d("PRINTER", tempstring + " temp  length ${tempstring.length}")

                val rowAndItemNameArray: ArrayList<String> = arrayListOf()
                var exitFlag = true


                do {

                    if (tempstring.length > seconCut) {

                        if (rowAndItemNameArray.size > 0) {
                            rowAndItemNameArray.add(
                                "     " +
                                        tempstring.substring(
                                            0,
                                            seconCut - 1
                                        ) + "\n"
                            )
                        } else {
                            rowAndItemNameArray.add(
                                tempstring.substring(
                                    0,
                                    seconCut - 1
                                ) + "\n"
                            )
                        }


                        tempstring =
                            tempstring.substring(seconCut - 1)
                    } else {
                        if (tempstring.isNotEmpty() && rowAndItemNameArray.isEmpty()) rowAndItemNameArray.add(
                            tempstring
                        ) else rowAndItemNameArray.add("     $tempstring")
                        exitFlag = false
                    }

                } while (exitFlag)

                var finalRowAndItemName = ""
                rowAndItemNameArray.forEach {
                    finalRowAndItemName += "[L]$it"
                }

                Log.d("PRINTER", "final row array" + finalRowAndItemName)

                val quantityString =
                    Utils.getIntNumberString(documentLines.Quantity!!).padStart(4)

                Log.wtf("PRICESTRING", documentLines.BasePriceUZS.toString())
                val priceString =
                    Utils.getIntNumberString(documentLines.BasePriceUZS)
                        .padStart(9)

                Log.d("PRINTER", "qty/price   ")

                docLinesText += "${finalRowAndItemName}[R]$quantityString X $priceString\n"

                if (documentLines.DiscountPercent > 0.0) {
                    val discountSum =
                        Utils.getIntNumberString((documentLines.BasePriceUZS!! - documentLines.PriceAfterVATUZS!!))
                            .padStart(9)

                    docLinesText += "***СКИДКА***[R] ${Utils.getIntNumberString(documentLines.DiscountPercent)}%  $discountSum\n"
                    docLinesText += "*СО СКИДКОЙ*[R]${Utils.getIntNumberString(documentLines.PriceAfterVATUZS!!)}\n"

                }
                docLinesText += getOneLineDividerString(Preferences.printerMaxChar) + "\n"

                docTotalWithoutDiscountUZS += documentLines.BasePriceUZS!! * documentLines.Quantity!!

            }

            // receipt with headers
            printer
                .printFormattedTextAndCut(
                    "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
                        printer, applicationContext.resources.getDrawableForDensity(
                            imageResource, DisplayMetrics.DENSITY_MEDIUM
                        )
                    ) + "</img>\n" +
                            //TODO RAMAZON TUGAGANDAN KN YOQISH KK
                            "[C]<b>${Preferences.receiptHeaderText}</b>" +
                            "[L]\n" +
                            "[C]<b>${Preferences.receiptShopText}</b>\n" +
                            "[L]\n" +
                            "[L]КЛИЕНТ: ${document.CardName}, $phone\n" +
                            "[L]\n" +
                            "[L]${docType}:[R]№${document.DocNum}\n" +
                            "[L]ДАТА: ${Utils.convertUSAdatetoNormal(document.DocDate!!)} ВРЕМЯ: ${document.DocTime}\n" +
                            "[L]МЕНЕДЖЕР: $managerName, ${managerMobile ?: ""}\n" +
                            getDividerString(Preferences.printerMaxChar) +
                            "[L]        НАИМЕНОВАНИЕ[R] КОЛ Х      ЦЕНА\n" +
                            getDividerString(Preferences.printerMaxChar) +
                            docLinesText +
                            "\n[L]<b>ИТОГО:[R]${
                                Utils.getNumberWithThousandSeparator(docTotalWithoutDiscountUZS)
                            }</b>\n" +
                            "[L]<b>СКИДКА:[R]${Utils.getNumberWithThousandSeparator((docTotalWithoutDiscountUZS - document.DocTotalUZS!!))}</b>\n" +
                            "[L]<b>К ОПЛАТЕ:[R]${Utils.getNumberWithThousandSeparator(document.DocTotalUZS!!)}</b>\n" +
                            getDividerString(Preferences.printerMaxChar) +
                            "[L]<b>ОПЛАЧЕНО:[R]${Utils.getNumberWithThousandSeparator(paidSum)}</b>\n" +
                            "[L]<b>В ДОЛГ:[R]${Utils.getNumberWithThousandSeparator(document.DocTotalUZS!! - paidSum)}</b>\n" +
                            "[L]\n" +
                            "[C]<b>${Preferences.receiptBottomText}</b>\n" +
                            getDividerString(Preferences.printerMaxChar) +
                            "[L]\n" + "[L]\n" + "[L]\n" + "[L]\n" +
                            "[L]\n", 100
                )

            printer.disconnectPrinter()

            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    fun generatePrintString(
        document: Document,
        phone: String,
        managerName: String,
        managerMobile: String?,
        paidSum: Double,
        isReturn: Boolean,
    ) {
        Log.wtf("PRINTERHELPER", document.toString())

        val docType = if (isReturn) "ВОЗВРАТ" else "ПРОДАЖА"
        val seconCut = Preferences.printerMaxChar - 18

        var docTotalWithoutDiscountUZS: Double = 0.0

        var docLinesText = ""
        // formatted document lines
        document.DocumentLines.forEachIndexed { index, documentLines ->
            val realIndex = index + 1
            val indexOffset =
                if (realIndex > 999) "$realIndex " else if (realIndex in 100..999) "$realIndex  " else if (realIndex in 10..99) "$realIndex   " else "$realIndex    "

            val rowAndItemName = "${indexOffset}${documentLines.ItemName}"
            var tempstring = rowAndItemName
            Log.d("PRINTER", tempstring + " temp  length ${tempstring.length}")

            val rowAndItemNameArray: ArrayList<String> = arrayListOf()
            var exitFlag = true


            do {

                if (tempstring.length > seconCut) {

                    if (rowAndItemNameArray.size > 0) {
                        rowAndItemNameArray.add(
                            "     " +
                                    tempstring.substring(
                                        0,
                                        seconCut - 1
                                    ) + "\n"
                        )
                    } else {
                        rowAndItemNameArray.add(
                            tempstring.substring(
                                0,
                                seconCut - 1
                            ) + "\n"
                        )
                    }


                    tempstring =
                        tempstring.substring(seconCut - 1)
                } else {
                    if (tempstring.isNotEmpty() && rowAndItemNameArray.isEmpty()) rowAndItemNameArray.add(
                        tempstring
                    ) else rowAndItemNameArray.add("     $tempstring")
                    exitFlag = false
                }

            } while (exitFlag)

            var finalRowAndItemName = ""
            rowAndItemNameArray.forEach {
                finalRowAndItemName += "[L]$it"
            }

            Log.d("PRINTER", "final row array" + finalRowAndItemName)

            val quantityString =
                Utils.getIntNumberString(documentLines.Quantity!!).padStart(4)
            val priceString =
                Utils.getIntNumberString(documentLines.BasePriceUZS!!)
                    .padStart(9)

            Log.d("PRINTER", "qty/price   ")




            docLinesText += "${finalRowAndItemName}[R]$quantityString X $priceString\n"

            if (documentLines.DiscountPercent > 0.0) {
                val discountSum =
                    Utils.getIntNumberString((documentLines.BasePriceUZS!! - documentLines.PriceAfterVATUZS!!))
                        .padStart(9)

                docLinesText += "***СКИДКА***[R]${Utils.getIntNumberString(documentLines.DiscountPercent)}% X $discountSum\n"
                docLinesText += "*СО СКИДКОЙ*[R]${Utils.getIntNumberString(documentLines.PriceAfterVATUZS!!)}\n"

            }
            docLinesText += getOneLineDividerString(Preferences.printerMaxChar) + "\n"

            docTotalWithoutDiscountUZS += documentLines.BasePriceUZS!! * documentLines.Quantity!!

        }


        val printtext =

            "[C]<b>${Preferences.receiptHeaderText}</b>" +
                    "[L]\n" +
                    "[C]<b>${Preferences.receiptShopText}</b>\n" +
                    "[L]\n" +
                    "[L]КЛИЕНТ: ${document.CardName}, $phone\n" +
                    "[L]\n" +
                    "[L]${docType}:[R]№${document.DocNum}\n" +
                    "[L]ДАТА: ${Utils.convertUSAdatetoNormal(document.DocDate!!)} ВРЕМЯ: ${document.DocTime}\n" +
                    "[L]МЕНЕДЖЕР: $managerName, ${managerMobile ?: ""}\n" +
                    getDividerString(Preferences.printerMaxChar) +
                    "[L]        НАИМЕНОВАНИЕ[R] КОЛ Х      ЦЕНА\n" +
                    getDividerString(Preferences.printerMaxChar) +
                    docLinesText +
                    "\n[L]<b>ИТОГО:[R]${
                        Utils.getNumberWithThousandSeparator(docTotalWithoutDiscountUZS)
                    }</b>\n" +
                    "\n[L]<b>СКИДКА:[R]${Utils.getNumberWithThousandSeparator((docTotalWithoutDiscountUZS - document.DocTotalUZS!!))}</b>\n" +
                    "\n[L]<b>К ОПЛАТЕ:[R]${Utils.getNumberWithThousandSeparator(document.DocTotalUZS!!)}</b>\n" +
                    getDividerString(Preferences.printerMaxChar) +
                    "[L]<b>ОПЛАЧЕНО:[R]${Utils.getNumberWithThousandSeparator(paidSum)}</b>\n" +
                    "[L]<b>В ДОЛГ:[R]${Utils.getNumberWithThousandSeparator(document.DocTotalUZS!! - paidSum)}</b>\n" +
                    "[L]\n" +
                    "[C]<b>${Preferences.receiptBottomText}</b>\n" +
                    getDividerString(Preferences.printerMaxChar) +
                    "[L]\n" + "[L]\n" + "[L]\n" + "[L]\n" +
                    "[L]\n"


        Log.wtf("PRINTER", printtext.toString())
    }


    fun printTest(
        printer: EscPosPrinter,
        applicationContext: Context,
    ): Boolean {

        try {

            // receipt with headers
            printer
                .printFormattedTextAndCut(
                    "[C]<b>ТЕСТОВАЯ СТРАНИЦА</b>\n" +
                            getDividerString(Preferences.printerMaxChar) +
                            "[L]\n" +
                            "[L]<b>ПОЛЬЗОВАТЕЛЬ: ${Preferences.userName}</b>\n" +
                            "[L]\n" +
                            "[L]МАГАЗИН: ${Preferences.defaultWhs}\n" +
                            "[L]\n" +
                            getDividerString(Preferences.printerMaxChar) +
                            "[L]\n" + "[L]\n" + "[L]\n" + "[L]\n" +
                            "[L]\n", 100
                )

            printer.disconnectPrinter()

            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    fun getDividerString(numberOfChars: Int): String {
        var resultString = "[L]"
        for (i in 1..numberOfChars) {
            resultString += "="
        }
        return resultString + "\n"
    }

    fun getOneLineDividerString(numberOfChars: Int): String {
        var resultString = "[L]"
        for (i in 1..numberOfChars) {
            resultString += "-"
        }
        return resultString
    }


}