package com.example.lorettocashback.presentation.history

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.R
import com.example.lorettocashback.core.BaseActivity
import com.example.lorettocashback.data.model.SimpleData
import com.example.lorettocashback.databinding.ActivityHistoryBinding
import com.example.lorettocashback.presentation.adapter.HistoryAdapter
import java.text.SimpleDateFormat
import java.util.Locale

val list = listOf(
    SimpleData(
        "FV87995SFG6",
        "14.10.2022",
        "2$"
    ),
    SimpleData(
        "FV80005SFG7",
        "15.10.2022",
        "5$"
    ),
    SimpleData(
        "FV000000000",
        "16.10.2022",
        "3$"
    ),
    SimpleData(
        "FV444444444",
        "17.10.2022",
        "1$"
    ),
    SimpleData(
        "FV222222222",
        "18.10.2022",
        "4$"
    ),
    SimpleData(
        "Ff44445SFG6",
        "19.10.2022",
        "7$"
    ),
    SimpleData(
        "FV879555556",
        "20.10.2022",
        "6$"
    ),
)

class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mViewModel: HistoryViewModel
    private lateinit var rvAdapter: HistoryAdapter

    override fun init(savedInstanceState: Bundle?) {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        rvAdapter = HistoryAdapter()
        val myCalendar = Calendar.getInstance()

        binding.list.adapter = rvAdapter
        binding.list.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        rvAdapter.submitList(list)

        rvAdapter.setClickListener {
            val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_history, null)
            val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
            dialog.show()
            dialog.window!!.setBackgroundDrawable(null)
        }

        binding.textDataC.setOnClickListener {
            openCalendar(myCalendar, datePickerFun(myCalendar, binding.textDataC))
        }

        binding.textDataPo.setOnClickListener {
            openCalendar(myCalendar, datePickerFun(myCalendar, binding.textDataPo))
        }

        mViewModel.textDate1.observe(this, textDateObserve1)
        mViewModel.textDate2.observe(this, textDateObserve2)

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
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        if (textView == binding.textDataC) {
            mViewModel.textDateFun1(sdf.format(myCalendar.time))
        } else {
            mViewModel.textDateFun2(sdf.format(myCalendar.time))
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

    private val textDateObserve1 = Observer<String> {
        binding.textDataC.text = it
    }

    private val textDateObserve2 = Observer<String> {
        binding.textDataPo.text = it
    }
}