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
import com.example.lorettocashback.data.model.StatusEnum
import com.example.lorettocashback.databinding.ActivityHistoryBinding
import com.example.lorettocashback.presentation.adapter.HistoryAdapter
import kotlinx.android.synthetic.main.activity_history.calendar1
import java.text.SimpleDateFormat
import java.util.Locale

val list = listOf(
    SimpleData(
        "LF - 230B",
        "2022-10-14",
        "2$",
        StatusEnum.CANCELLED,
        "Cancelled"
    ),
    SimpleData(
        "LF - 230B",
        "2022-10-14",
        "2$",
        StatusEnum.GAINED,
        "Gained"
    ),
    SimpleData(
        "LF - 230B",
        "2022-10-14",
        "2$",
        StatusEnum.WITHDREW,
        "WithDrew"
    ),
    SimpleData(
        "LF - 230B",
        "2022-10-14",
        "2$",
        StatusEnum.CANCELLED,
        "Cancelled"
    ),
    SimpleData(
        "LF - 230B",
        "2022-10-14",
        "2$",
        StatusEnum.RETURNED,
        "Returned"
    )
)

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

        binding.calendar1.setOnClickListener {
            openCalendar(myCalendar, datePickerFun(myCalendar, binding.textDataC))
        }

        binding.calendar2.setOnClickListener {
            openCalendar(myCalendar, datePickerFun(myCalendar, binding.textDataPo))
        }

        binding.cancelBnt.setOnClickListener {
            mViewModel.clickCancelFun()
        }

        mViewModel.textDate1.observe(this, textDateObserve1)
        mViewModel.textDate2.observe(this, textDateObserve2)
        mViewModel.clickCancel.observe(this, clickCancelObserve)

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
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        if (textView == binding.textDataC) {
            mViewModel.textDate1Fun(sdf.format(myCalendar.time))
        } else {
            mViewModel.textDate2Fun(sdf.format(myCalendar.time))
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

    private val clickCancelObserve = Observer<Unit> {
        myCalendar.set(Calendar.YEAR, myCalendar1.get(Calendar.YEAR))
        myCalendar.set(Calendar.MONTH, myCalendar1.get(Calendar.MONTH))
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar1.get(Calendar.DAY_OF_MONTH))
        binding.textDataC.text = "Выбирать ..."
        binding.textDataPo.text = "Выбирать ..."
    }
}