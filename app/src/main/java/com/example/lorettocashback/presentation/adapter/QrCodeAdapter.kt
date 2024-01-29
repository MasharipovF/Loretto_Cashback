package com.example.lorettocashback.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.data.model.SimpleData
import com.example.lorettocashback.databinding.ItemQrCodeBinding

class QrCodeAdapter : ListAdapter<SimpleData, QrCodeAdapter.EventHolder>(EventDiffUtil) {

    private var clickListener: ((SimpleData) -> Unit)? = null

    object EventDiffUtil : DiffUtil.ItemCallback<SimpleData>() {
        override fun areItemsTheSame(
            oldItem: SimpleData,
            newItem: SimpleData
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: SimpleData,
            newItem: SimpleData
        ): Boolean {
            return oldItem.name == newItem.name
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

                binding.textNum.text = this.name
                binding.textSum.text = this.sum
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

    fun setClickListener(block: (SimpleData) -> Unit) {
        clickListener = block
    }

}