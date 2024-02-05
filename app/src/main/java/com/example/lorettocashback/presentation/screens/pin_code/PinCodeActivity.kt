package com.example.lorettocashback.presentation.screens.pin_code

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.entity.businesspartners.CashbackUsers
import com.example.lorettocashback.databinding.ActivityPinCodeBinding
import com.example.lorettocashback.presentation.screens.login.LoginActivity
import com.example.lorettocashback.presentation.screens.main.MainActivity
import com.example.lorettocashback.presentation.screens.register_pincode.RegisterPinCodeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PinCodeActivity : BaseActivity() {
    lateinit var binding: ActivityPinCodeBinding
    private var code = StringBuilder("")
    private lateinit var mViewModel: PinCodeViewModel
    private var logic = true
    private lateinit var vibrator: Vibrator
    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(PinCodeViewModel::class.java)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.apply {

            keyboard.btOne.setOnClickListener {
                setNumber("1", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btTwo.setOnClickListener {
                setNumber("2", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btThree.setOnClickListener {
                setNumber("3", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btFour.setOnClickListener {
                setNumber("4", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btFive.setOnClickListener {
                setNumber("5", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btSix.setOnClickListener {
                setNumber("6", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btSeven.setOnClickListener {
                setNumber("7", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btEight.setOnClickListener {
                setNumber("8", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btNine.setOnClickListener {
                setNumber("9", firstNum, secondNum, thirdNum, fourthNum)
            }
            keyboard.btZero.setOnClickListener {
                setNumber("0", firstNum, secondNum, thirdNum, fourthNum)
            }

        }

        onClick()

        mViewModel.loggedUser.observe(this, loggedUserObserve)
        mViewModel.loginError.observe(this, loginErrorObserve)
        mViewModel.loading.observe(this, loadingObserve)
    }

    private fun onClick() {
        binding.apply {
            keyboard.btClear.setOnClickListener {

                if (fourthNum.text.isNotEmpty()) {
                    fourthNum.text = ""
                    code.deleteCharAt(code.length - 1)
                } else if (thirdNum.text.isNotEmpty()) {
                    thirdNum.text = ""
                    code.deleteCharAt(code.length - 1)
                } else if (secondNum.text.isNotEmpty()) {
                    secondNum.text = ""
                    code.deleteCharAt(code.length - 1)
                } else if (firstNum.text.isNotEmpty()) {
                    firstNum.text = ""
                    code.deleteCharAt(code.length - 1)
                }

            }

            signOutBtn.setOnClickListener {
                Preferences.pinCode = null
                val intent = Intent(this@PinCodeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun setNumber(
        str: String,
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
        textView4: TextView
    ) {
        if (logic) {

            if (textView1.text.isEmpty()) {
                textView1.text = str
                code.append(str)
            } else if (textView2.text.isEmpty()) {
                textView2.text = str
                code.append(str)
            } else if (textView3.text.isEmpty()) {
                textView3.text = str
                code.append(str)
            } else if (textView4.text.isEmpty()) {
                textView4.text = str
                code.append(str)
            }
        }


        if (code.length == 4) {
            lifecycleScope.launch {
                delay(100)
                checkPinCode(code.toString())
                textView1.text = ""
                textView2.text = ""
                textView3.text = ""
                textView4.text = ""
                code.clear()
            }
        }
    }

    private fun checkPinCode(pinCode: String) {
        logic = false
        mViewModel.requestLogin(pinCode)
    }


    private val loggedUserObserve = Observer<CashbackUsers> {
        if (it != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(GeneralConsts.INTENT_EXTRA_BUSINESS_PARTNERS, it)
            startActivity(intent)
            finish()
        }
    }

    private val loginErrorObserve = Observer<String> {
        showSnackbar(
            binding.container,
            "Ошибка $it",
            R.color.red
        )
        logic = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(100)
        }
    }

    private val loadingObserve = Observer<Boolean> {
        if (it) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}