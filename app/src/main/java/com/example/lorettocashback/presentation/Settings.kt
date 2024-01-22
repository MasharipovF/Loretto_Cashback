package com.example.lorettocashback.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.databinding.ActivitySettingsBinding
import com.example.lorettocashback.util.xprinter.PrinterHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Settings : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding


    override fun init(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.etvBranch.setText(Preferences.branch.toString())
        binding.etvDefCustomer.setText(Preferences.defaultCustomer.toString())
        binding.etvShop.setText(Preferences.defaultWhs.toString())
        binding.etvAcctUSD.setText(Preferences.accountUSD.toString())
        binding.etvAcctUZS.setText(Preferences.accountUZS.toString())
        binding.etvUsername.setText(Preferences.userName.toString())

        binding.noPaginationCheck.isChecked = Preferences.noPaginationConfigutation?:false

        binding.etvPrinterIpAddress.setText(Preferences.printerIp)
        binding.etvPrinterPort.setText(Preferences.printerPort.toString())
        binding.etvPrinterMaxChar.setText(Preferences.printerMaxChar.toString())
        binding.etvPrinterDPI.setText(Preferences.printerDPI.toString())
        binding.etvPrinterWidth.setText(Preferences.printerWidth.toString())
        binding.etvHeaderText.setText(Preferences.receiptHeaderText)
        binding.etvBottomText.setText(Preferences.receiptBottomText)
        binding.etvShopText.setText(Preferences.receiptShopText)




        binding.btnSubmit.setOnClickListener {
            Preferences.printerIp = binding.etvPrinterIpAddress.text.toString()
            Preferences.printerPort = binding.etvPrinterPort.text.toString().toInt()
            Preferences.receiptHeaderText = binding.etvHeaderText.text.toString()
            Preferences.receiptBottomText = binding.etvBottomText.text.toString()
            Preferences.receiptShopText = binding.etvShopText.text.toString()

            Preferences.printerDPI = binding.etvPrinterDPI.text.toString().toInt()
            Preferences.printerWidth = binding.etvPrinterWidth.text.toString().toInt()
            Preferences.printerMaxChar = binding.etvPrinterMaxChar.text.toString().toInt()
            Preferences.noPaginationConfigutation = binding.noPaginationCheck.isChecked
            finish()
        }

        binding.btnPrintTest.setOnClickListener {
            Preferences.printerIp = binding.etvPrinterIpAddress.text.toString()
            Preferences.printerPort = binding.etvPrinterPort.text.toString().toInt()
            Preferences.receiptHeaderText = binding.etvHeaderText.text.toString()
            Preferences.receiptBottomText = binding.etvBottomText.text.toString()
            Preferences.receiptShopText = binding.etvShopText.text.toString()

            Preferences.printerDPI = binding.etvPrinterDPI.text.toString().toInt()
            Preferences.printerWidth = binding.etvPrinterWidth.text.toString().toInt()
            Preferences.printerMaxChar = binding.etvPrinterMaxChar.text.toString().toInt()

            if (checkForFilling())
                connectAndPrint(this)
            else
                Toast.makeText(this, "Заполните IP и порт принтера!", Toast.LENGTH_SHORT).show()

        }
    }


    fun checkForFilling(): Boolean {
        return !(Preferences.printerIp.isNullOrEmpty() || Preferences.portNumber.isNullOrEmpty())
    }


    private fun showSnackbar(showstring: String) {
        Snackbar.make(binding.layoutMainSettings, showstring, Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(R.color.button_unable)).show()
    }

    fun connectAndPrint(
        context: Context
    ) {
        GlobalScope.launch {
            try {
                val printer =
                    EscPosPrinter(
                        TcpConnection(
                            Preferences.printerIp,
                            Preferences.printerPort,
                            GeneralConsts.PRINTER_TIMEOUT
                        ),
                        Preferences.printerDPI,
                        Preferences.printerWidth.toFloat(),
                        Preferences.printerMaxChar,
                        EscPosCharsetEncoding(
                            GeneralConsts.PRINTER_CHARSET,
                            GeneralConsts.PRINTER_CHARSET_CODE
                        )
                    )

                val printingResults = PrinterHelper.printTest(
                    printer,
                    context
                )

                Log.d("PRINTER", "ПРИНТЕР ПОДКЛЮЧЕН")

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }


}