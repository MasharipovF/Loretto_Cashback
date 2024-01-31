package com.example.lorettocashback.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.databinding.ItemQrCodeBinding

class QrCodeAdapter : ListAdapter<CashbackQrCode, QrCodeAdapter.EventHolder>(EventDiffUtil) {

    private var clickListener: ((CashbackQrCode) -> Unit)? = null

    object EventDiffUtil : DiffUtil.ItemCallback<CashbackQrCode>() {
        override fun areItemsTheSame(
            oldItem: CashbackQrCode,
            newItem: CashbackQrCode
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: CashbackQrCode,
            newItem: CashbackQrCode
        ): Boolean {
            return oldItem.itemName == newItem.itemName || oldItem.serialNumber == newItem.serialNumber
        }
    }

    inner class EventHolder(private val binding: ItemQrCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind() {
            getItem(absoluteAdapterPosition).apply {

                binding.textNum.text = this.itemName
                binding.textSum.text = this.cashbackAmount.toString()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder = EventHolder(
        ItemQrCodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bind()
    }

    fun setClickListener(block: (CashbackQrCode) -> Unit) {
        clickListener = block
    }

}