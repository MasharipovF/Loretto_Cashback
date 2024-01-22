package com.example.lorettocashback.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.databinding.ActivityMainBinding
import com.example.lorettocashback.util.enums.ActivityBpTypes
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.*


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mViewModel: MainActivityViewModel

    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        supportActionBar?.title = resources.getString(R.string.doctype_main)


        mViewModel.connectionError.observe(this) {
            Toast.makeText(
                this,
                "Connection error: " + mViewModel.errorString.toString(),
                Toast.LENGTH_SHORT
            ).show()

            if (mViewModel.loading.value == true) {
                mViewModel.loading.value = false
            }

        }


        /* mViewModel.getExchangeRate()
         showLoader(this)*/

        mViewModel.appVersion.observe(this) {
            if (it != null) {
                (this as BaseActivity).showAlertAppVersionIncompatible(it)
            }
        }


    }

   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }*/


    override fun onResume() {
        super.onResume()
        mViewModel.getAppVersion()
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun startRepeatingJob(intervalInMs: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (NonCancellable.isActive) {
                delay(intervalInMs)
                Log.wtf("REPEATING_JOB", "LALA")
            }
        }
    }

   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_user_settings -> {
                val intent: Intent = Intent(this, Settings::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_exchange -> {
                // mViewModel.getExchangeRate()
                // mViewModel.getDiscountByDocTotal()
                // showLoader(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

/*
    private fun showLoader(activity: Activity) {
        val dialog = Dialog(activity)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_loader)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val loader = dialog.findViewById<LottieAnimationView>(R.id.loader)
        val btnReload = dialog.findViewById<Button>(R.id.btnReload)

        dialog.setOnCancelListener {
            finish()
        }
        mViewModel.loading.observe(this) {
            if (it) {
                loader.visibility = View.VISIBLE
                btnReload.visibility = View.GONE
            } else {
                loader.visibility = View.GONE
                btnReload.visibility = View.VISIBLE

            }
        }

        mViewModel.exchangeRate.observe(this) {
            Preferences.currencyDate = Utils.getCurrentDateinUSAFormat()
            Preferences.currencyRate = it
            dialog.dismiss()
        }

        mViewModel.errorItem.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        btnReload.setOnClickListener {
            mViewModel.getExchangeRate()
        }

        dialog.show()
        DialogSizeUtils.resizeDialog(dialog, this, 70, 30)
    }
*/





}