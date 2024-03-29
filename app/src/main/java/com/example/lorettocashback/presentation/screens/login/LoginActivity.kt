package com.example.lorettocashback.presentation.screens.login

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.core.GeneralConsts.INTENT_EXTRA_BUSINESS_PARTNERS
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.databinding.ActivityLoginBinding
import com.example.lorettocashback.presentation.screens.main.MainActivity
import com.example.lorettocashback.presentation.screens.pin_code.PinCodeActivity
import com.example.lorettocashback.presentation.screens.register_pincode.RegisterPinCodeActivity


class LoginActivity : BaseActivity() {

    private lateinit var mViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun init(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)


        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)



        if (!isIpAddressWritten()) {
            showIpAddressDialog(this)
        }

        if (Preferences.pinCode != null) {
            val intent = Intent(this, PinCodeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mViewModel.connectionError.observe(this) {
            Toast.makeText(this, "Connection error: " + mViewModel.errorString, Toast.LENGTH_SHORT)
                .show()
            binding.textView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isClickable = true
            binding.btnLogin.isFocusable = true
        }

        mViewModel.loading.observe(this) {
            if (it) {
                binding.textView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLogin.isClickable = false
                binding.btnLogin.isFocusable = false
            } else {
                binding.textView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isClickable = true
                binding.btnLogin.isFocusable = true
            }
        }

        mViewModel.loggedUser.observe(this) {
            if (it != null) {
                val intent = Intent(this, RegisterPinCodeActivity::class.java)
                intent.putExtra(INTENT_EXTRA_BUSINESS_PARTNERS, it)
                startActivity(intent)
                finish()
            }
        }

        mViewModel.loginerror.observe(this) {
            showSnackbar(
                binding.container,
                "Ошибка $it",
                R.color.red
            )
        }

        binding.versionTv.text = getString(R.string.app_version, GeneralConsts.APP_VERSION)

        binding.btnLogin.setOnClickListener {

            if (!isIpAddressWritten()) {
                showIpAddressDialog(this)
                return@setOnClickListener
            }

            val username = binding.etvLogin.text.toString()
            val password = binding.etvPassword.text.toString()

            if (username == "") {
                binding.tilLogin.error = getString(R.string.askUsername)
                return@setOnClickListener
            } else binding.tilLogin.error = null

            if (password == "") {
                binding.tilPassword.error = getString(R.string.askPassword)
                return@setOnClickListener
            } else binding.tilPassword.error = null

            mViewModel.requestLogin(phone = username, password = password)

        }



        binding.etvPassword.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.btnLogin.performClick()
                true
            }
            false
        }

        binding.imgBtnSettings.setOnClickListener {
            showIpAddressDialog(this)
        }


    }

    private fun isIpAddressWritten(): Boolean {
        if (Preferences.ipAddress.isNullOrEmpty()) return false
        if (Preferences.portNumber.isNullOrEmpty()) return false
        return !Preferences.companyDB.isNullOrEmpty()
    }

    private fun showIpAddressDialog(activity: Activity) {
        val dialog = Dialog(activity)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_database_ip)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
        val etvIp = dialog.findViewById<EditText>(R.id.etvipAddress)
        val etvPort = dialog.findViewById<EditText>(R.id.etvPortNumber)
        val etvDbName = dialog.findViewById<EditText>(R.id.etvCompanyDb)

        etvIp.setText(Preferences.ipAddress ?: "")
        etvPort.setText(Preferences.portNumber ?: "")
        etvDbName.setText(Preferences.companyDB ?: "")

        Log.d(
            "LOGINREPREFERENCES",
            "${Preferences.ipAddress}      ${Preferences.portNumber}      ${Preferences.companyDB}      "
        )

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            Preferences.ipAddress = etvIp.text.toString()
            Preferences.portNumber = etvPort.text.toString()
            Preferences.companyDB = etvDbName.text.toString()
            dialog.dismiss()
            Log.d(
                "LOGINREPREFERENCES",
                "${Preferences.ipAddress}      ${Preferences.portNumber}      ${Preferences.companyDB}      "
            )
        }
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


}