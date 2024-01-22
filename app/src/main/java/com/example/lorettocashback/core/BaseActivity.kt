package com.example.lorettocashback.core

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.lorettocashback.R
import com.example.lorettocashback.core.TrialTimeOut.TRIAL_TIMEOUT_DATE
import com.example.lorettocashback.data.entity.masterdatas.AppVersion
import com.example.lorettocashback.util.barcodereader.BarCodeScannerHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    var repeatingJob: Job? = null

    override fun onResume() {
        super.onResume()
        repeatingJob = startRepeatingJob(10000L)
    }

    override fun onPause() {
        super.onPause()
        repeatingJob?.cancel()
    }

    open fun startRepeatingJob(intervalInMs: Long): Job? {
        return null
    }

    lateinit var barcodeScannerHelper: BarCodeScannerHelper


    abstract fun init(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)


        barcodeScannerHelper = BarCodeScannerHelper()
        //showAlertDialogTrialTimeout()

        init(savedInstanceState)
    }


    private fun showAlertDialogTrialTimeout() {
        if (SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
                .toString() >= TRIAL_TIMEOUT_DATE
        ) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Период тестирования закончился")
                .setMessage("Обратитесь к администратору для продления пробного периода")
                .setCancelable(false)
                .setPositiveButton("ОК") { _, _ ->
                    finish()
                }
                .create()
                .show()
        }


    }


    fun showAlertAppVersionIncompatible(appVersion: AppVersion) {
        val mDate = if (appVersion.uFromDate.length > 10) appVersion.uFromDate.substring(
            0,
            10
        ) else appVersion.uFromDate



        if (SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
                .toString() >= mDate && appVersion.version != GeneralConsts.APP_VERSION
        ) {
            val dialog = Dialog(this)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_app_version_incompatible)
            val btnClose = dialog.findViewById<Button>(R.id.btnCloseApp)
            val tvLink = dialog.findViewById<TextView>(R.id.tvLink)

            tvLink.text = appVersion.uLink

            btnClose.setOnClickListener {
                finishAffinity()
            }

            dialog.show()

        }
    }



    fun replaceFragment(
        containerId: Int,
        fragment: Fragment,
        tag: String,
        backStack: Boolean = false,
        transition: Int = FragmentTransaction.TRANSIT_NONE,
    ) {
        val foundFragment = supportFragmentManager.findFragmentByTag(tag)
        Log.d("FRAGMENT", "REPLACED ${foundFragment}")

        if (foundFragment != null && foundFragment.isAdded) return

        val transaction = supportFragmentManager.beginTransaction()
        if (transition != FragmentTransaction.TRANSIT_NONE)
            transaction.setTransition(transition)
        transaction.replace(containerId, fragment, tag)
        if (backStack)
            transaction.addToBackStack(tag)
        transaction.commit()
    }


    fun addFragment(
        containerId: Int,
        fragment: Fragment,
        tag: String,
        backStack: Boolean = false,
        transition: Int = FragmentTransaction.TRANSIT_NONE,
    ) {
        val foundFragment = supportFragmentManager.findFragmentByTag(tag)
        Log.d("FRAGMENT", "ADDED ${foundFragment}")

        if (foundFragment != null && foundFragment.isAdded) {
            if (foundFragment.isHidden) {
                supportFragmentManager.beginTransaction().show(foundFragment).commitNow()
            }
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (transition != FragmentTransaction.TRANSIT_NONE)
            transaction.setTransition(transition)
        transaction.add(containerId, fragment, tag)
        if (backStack)
            transaction.addToBackStack(tag)
        transaction.commit()
    }

    override fun onBackPressed() {

        var hasChanges: Boolean = false

        when (this) {

            /*is CreditNoteInfoActivity -> {
                hasChanges = this.hasChanges()
                if (hasChanges) showAlertDialog()
                else super.onBackPressed()
            }
            is InvoiceInfoActivity -> {
                hasChanges = this.hasChanges()
                if (hasChanges) showAlertDialog()
                else super.onBackPressed()
            }*/

            else -> super.onBackPressed()
        }

        /*if (supportFragmentManager.backStackEntryCount > 1) {
            val index = supportFragmentManager.backStackEntryCount - 1
            val backEntry = supportFragmentManager.getBackStackEntryAt(index);
            val tag = backEntry.name;
            val fragment = supportFragmentManager.findFragmentByTag(tag);
            super.onBackPressed()

        } else {
            if (!hasChanges) finish()
        }*/

        if (supportFragmentManager.backStackEntryCount == 0) {
            if (!hasChanges) finish()
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.doc_changed)
            .setMessage(R.string.doc_resetchanges)
            .setNegativeButton("Назад") { _, _ ->

            }
            .setPositiveButton("Сбросить") { _, _ ->
                finish()
            }
            .create()
            .show()
    }


    fun showSnackbar(container: View, message: String, colorResource: Int) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG)
            .setActionTextColor(
                ResourcesCompat.getColor(
                    resources,
                    colorResource,
                    null
                )
            ).show()
    }
}