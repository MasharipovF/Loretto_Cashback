package com.example.lorettocashback.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.entity.businesspartners.CashbackUsers
import com.example.lorettocashback.databinding.ActivityMainBinding
import com.example.lorettocashback.presentation.history.HistoryActivity
import com.example.lorettocashback.presentation.login.LoginActivity
import com.example.lorettocashback.presentation.notification.NotificationActivity
import com.example.lorettocashback.presentation.qr_code.QrCodeActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.*


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mViewModel: MainActivityViewModel

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val bundle: Bundle? = intent.extras

        val data =
            bundle?.getParcelable(GeneralConsts.INTENT_EXTRA_BUSINESS_PARTNERS) as CashbackUsers?

        binding.textName.text = "${data?.fullName}!"
        binding.textTip.text = "${data?.userTypeName}"
        binding.totalBonus.text = "${data?.currentCashback} $"
        binding.totalMonth.text = "${data?.gainedCashback} $"
        binding.totalWithdraw.text = "${data?.withdrewCashback} $"

        mViewModel.exitBtn.observe(this, exitScreenObserver)
        mViewModel.openNotificationBtn.observe(this, notificationScreenObserver)
        mViewModel.openQRBtn.observe(this, qrCodeScreenObserver)
        mViewModel.openHistoryBtn.observe(this, historyScreenObserver)

        binding.exitBtn.setOnClickListener {
            mViewModel.exitFun()
        }

        binding.notificationBtn.setOnClickListener {
            mViewModel.openNotificationFun()
        }

        binding.qrBtn.setOnClickListener {
            mViewModel.openQRFun()
        }

        binding.historyBtn.setOnClickListener {
            mViewModel.openHistoryFun()
        }
    }

    private val exitScreenObserver = Observer<Unit> {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val notificationScreenObserver = Observer<Unit> {
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    private val qrCodeScreenObserver = Observer<Unit> {
        val intent = Intent(this, QrCodeActivity::class.java)
        startActivity(intent)
    }

    private val historyScreenObserver = Observer<Unit> {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

}