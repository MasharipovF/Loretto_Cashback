package com.example.lorettocashback.presentation.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.data.entity.history.CashbackAmount
import com.example.lorettocashback.data.model.StatusEnum
import com.example.lorettocashback.databinding.ActivityHistoryBinding
import com.example.lorettocashback.databinding.DialogHistoryBinding
import com.example.lorettocashback.presentation.SapMobileApplication
import com.example.lorettocashback.presentation.adapter.HistoryAdapter
import java.text.SimpleDateFormat
import java.util.Locale


class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mViewModel: HistoryViewModel
    private lateinit var rvAdapter: HistoryAdapter
    private lateinit var myCalendar: Calendar
    private lateinit var myCalendar1: Calendar


    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        rvAdapter = HistoryAdapter()
        myCalendar = Calendar.getInstance()
        myCalendar1 = Calendar.getInstance()

        mViewModel.listDateQuery()

        binding.list.adapter = rvAdapter
        binding.list.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        onClick()

        binding.calendar1.setOnClickListener {
            openCalendar(myCalendar, datePickerFun(myCalendar, binding.textDataC))
        }

        binding.calendar2.setOnClickListener {
            openCalendar(myCalendar, datePickerFun(myCalendar, binding.textDataPo))
        }

        binding.cancelBnt.setOnClickListener {
            mViewModel.clickCancelFun()
        }

        mViewModel.textDateDe.observe(this, textDateDeObserve)
        mViewModel.textDateLe.observe(this, textDateLeObserve)
        mViewModel.clickCancel.observe(this, clickCancelObserve)
        mViewModel.listData.observe(this, listDataObserve)
        mViewModel.loading.observe(this, loadingObserve)
        mViewModel.errorData.observe(this, errorDataObserve)
        mViewModel.errorDataSum.observe(this, errorDataObserve)
        mViewModel.cashbackDataWithdrew.observe(this, cashbackDataWithdrewObserve)
        mViewModel.cashbackDataAll.observe(this, cashbackDataAllObserve)

    }

    @SuppressLint("SetTextI18n")
    private fun onClick() {
        rvAdapter.setClickListener { data ->
            val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_history, null)
            val binding = DialogHistoryBinding.bind(view)
            binding.itemNameDg.text = data.itemName
            binding.textDateDg.text = data.date
            binding.cashbackAmountDg.text = data.cashbackAmount.toString() + "$"
            binding.itemStatusDg.text = data.operationType
            binding.itemGroupNameDg.text = data.itemsGroupName
            binding.itemCodeTextDg.text = data.itemCode


            binding.itemStatusDg.setBackgroundResource(
                StatusEnum.values()
                    .find { it.statusName == data.operationType }!!.colorItemEnum
            )
            binding.itemStatusDg.setTextColor(
                ContextCompat.getColor(
                    SapMobileApplication.context,
                    StatusEnum.values()
                        .find { it.statusName == data.operationType }!!.textColorEnum
                )
            )

            val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
            dialog.show()
            dialog.window!!.setBackgroundDrawable(null)
        }

        rvAdapter.loadMore {
            mViewModel.listDateQuery(it)
        }
    }

    private fun openCalendar(myCalendar: Calendar, datePicker: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            this@HistoryActivity,
            datePicker,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateLabel(myCalendar: Calendar, textView: TextView) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        if (textView == binding.textDataC) {
            mViewModel.textDateDeFun(sdf.format(myCalendar.time))
        } else {
            mViewModel.textDateLeFun(sdf.format(myCalendar.time))
        }

        if (binding.textDataC.text.toString() != "Выбирать ..." && binding.textDataPo.text.toString() == "Выбирать ...") {
            mViewModel.listDateQuery(
                dateDe = binding.textDataC.text.toString()
            )
        }

        if (binding.textDataC.text.toString() == "Выбирать ..." && binding.textDataPo.text.toString() != "Выбирать ...") {
            mViewModel.listDateQuery(
                dateLe = binding.textDataPo.text.toString()
            )
        }

        if (binding.textDataC.text.toString() != "Выбирать ..." && binding.textDataPo.text.toString() != "Выбирать ...") {
            mViewModel.listDateQuery(
                dateDe = binding.textDataC.text.toString(),
                dateLe = binding.textDataPo.text.toString()
            )
        }

    }

    private fun datePickerFun(
        myCalendar: Calendar,
        textView: TextView
    ): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel(myCalendar, textView)
        }
    }

    private val textDateDeObserve = Observer<String> {
        binding.textDataC.text = it
    }

    private val textDateLeObserve = Observer<String> {
        binding.textDataPo.text = it
    }

    private val listDataObserve = Observer<List<Any>> { listData ->
        rvAdapter.submitList(listData)
    }

    private val clickCancelObserve = Observer<Unit> {
        if (binding.textDataC.text != "Выбирать ..." || binding.textDataPo.text != "Выбирать ...") {
            myCalendar.set(Calendar.YEAR, myCalendar1.get(Calendar.YEAR))
            myCalendar.set(Calendar.MONTH, myCalendar1.get(Calendar.MONTH))
            myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar1.get(Calendar.DAY_OF_MONTH))
            binding.textDataC.text = "Выбирать ..."
            binding.textDataPo.text = "Выбирать ..."
            mViewModel.listDateQuery()
        }
    }


    private val loadingObserve = Observer<Boolean> {
        if (it) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private val errorDataObserve = Observer<String> {
        showSnackbar(
            binding.container,
            "Ошибка $it",
            R.color.red
        )
    }

    @SuppressLint("SetTextI18n")
    private val cashbackDataWithdrewObserve = Observer<CashbackAmount> {
        binding.textWithDrew.text = it.cashbackAmount.toString() + "$"
    }

    @SuppressLint("SetTextI18n")
    private val cashbackDataAllObserve = Observer<CashbackAmount> {
        binding.textTotalSum.text = it.cashbackAmount.toString() + "$"
    }

}