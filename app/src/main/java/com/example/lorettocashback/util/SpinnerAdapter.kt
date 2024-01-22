package com.example.lorettocashback.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.lorettocashback.R
import com.example.lorettocashback.util.enums.ActivityStatus

class SpinnerAdapter(val context: Context) : BaseAdapter() {

    var list: MutableList<Any> = arrayListOf<Any>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val inflater = LayoutInflater.from(context)


    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = convertView ?: inflater.inflate(R.layout.list_spinner, parent, false)

        val item = list[position]
        val tvName = view.findViewById<TextView>(R.id.tvName)
        tvName.text = when (item) {
            is ActivityStatus -> item.statusName
            else -> ""
        }
        return view
    }


}