package com.example.lorettocashback.presentation.screens.register_pincode

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.entity.businesspartners.CashbackUsers
import com.example.lorettocashback.databinding.ActivityRegisterPinCodeBinding
import com.example.lorettocashback.presentation.screens.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterPinCodeActivity : BaseActivity() {
    lateinit var binding: ActivityRegisterPinCodeBinding
    private var code = StringBuilder("")
    private var code2 = StringBuilder("")
    private lateinit var mViewModel: RegisterPinCodeViewModel
    private lateinit var vibrator: Vibrator

    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityRegisterPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(RegisterPinCodeViewModel::class.java)
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

        }
    }

    private fun setNumber(
        str: String,
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
        textView4: TextView
    ) {
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
        if (code.length == 4) {
            lifecycleScope.launch {
                delay(100)
                check()
                textView1.text = ""
                textView2.text = ""
                textView3.text = ""
                textView4.text = ""
            }
        }
    }

    private fun check() {
        if (code2.length == 4) {
            val text1 = code.toString()
            val text2 = code2.toString()
            if (text1 == text2) {
                mViewModel.savePinCode(text1)
                val bundle: Bundle? = intent.extras

                val data =
                    bundle?.getParcelable(GeneralConsts.INTENT_EXTRA_BUSINESS_PARTNERS) as CashbackUsers?

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(GeneralConsts.INTENT_EXTRA_BUSINESS_PARTNERS, data)
                startActivity(intent)
                finish()
            } else {
                code.clear()
                code2.clear()
                binding.text.setText(R.string.create_pin_code)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val vibrationEffect =
                        VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)
                } else {
                    vibrator.vibrate(100)
                }
            }
        } else {
            val text = code.toString()
            code2.append(text)
            code.clear()
            Log.d("OOIIPP", "$code")
            binding.text.setText(R.string.re_enter_pin_code)
        }
    }

}