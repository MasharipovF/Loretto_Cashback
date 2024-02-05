package com.example.lorettocashback.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.data.entity.qr_code.CashbackQrCode
import com.example.lorettocashback.databinding.ItemQrCodeBinding
import com.example.lorettocashback.util.Utils

class QrCodeAdapter : ListAdapter<CashbackQrCode, QrCodeAdapter.EventHolder>(EventDiffUtil) {

    private var clickListener: ((CashbackQrCode) -> Unit)? = null
    private var deleteClickListener: ((CashbackQrCode) -> Unit)? = null

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
            binding.deleteBtn.setOnClickListener {
                deleteClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            getItem(absoluteAdapterPosition).apply {

                binding.textNum.text = this.itemName
                binding.serialNumber.text = this.serialNumber
                binding.textSum.text =
                    Utils.getNumberWithThousandSeparator(this.cashbackAmount) + "$"

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

    fun setDeleteClickListener(block: (CashbackQrCode) -> Unit) {
        deleteClickListener = block
    }

}