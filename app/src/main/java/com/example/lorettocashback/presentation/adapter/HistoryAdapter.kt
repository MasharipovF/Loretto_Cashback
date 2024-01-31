package com.example.lorettocashback.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lorettocashback.data.entity.history.CashbackHistory
import com.example.lorettocashback.data.model.StatusEnum
import com.example.lorettocashback.databinding.BottomLoaderRecyclerBinding
import com.example.lorettocashback.databinding.ItemHistoryBinding
import com.example.lorettocashback.presentation.SapMobileApplication
import com.example.lorettocashback.util.LoadMore

class HistoryAdapter : ListAdapter<Any, RecyclerView.ViewHolder>(EventDiffUtil) {

    private var clickListener: ((CashbackHistory) -> Unit)? = null
    private var load: ((Int) -> Unit)? = null

    object EventDiffUtil : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return if (oldItem is CashbackHistory && newItem is CashbackHistory) {
                oldItem.itemName == newItem.itemName || oldItem.serialNumber == newItem.serialNumber
            } else {
                false
            }
        }

    }

    private inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickListener?.invoke(getItem(absoluteAdapterPosition) as CashbackHistory)
            }

        }

        fun bind() {
            getItem(absoluteAdapterPosition).apply {

                if (this is CashbackHistory) {

                    binding.itemName.text = this.itemName
                    binding.textDate.text = this.date
                    binding.cashbackAmount.text = this.cashbackAmount.toString()
                    binding.statusItem.text = this.operationType

                    binding.statusItem.setBackgroundResource(
                        StatusEnum.values()
                            .find { it.statusName == this.operationType }!!.colorItemEnum
                    )
                    binding.statusItem.setTextColor(
                        ContextCompat.getColor(
                            SapMobileApplication.context,
                            StatusEnum.values()
                                .find { it.statusName == this.operationType }!!.textColorEnum
                        )
                    )
                }

            }
        }
    }

    private inner class LoadMoreViewHolder(private val binding: BottomLoaderRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            load?.invoke(absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> HistoryViewHolder(
                ItemHistoryBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> {
                LoadMoreViewHolder(
                    BottomLoaderRecyclerBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> (holder as HistoryViewHolder).bind()
            else -> (holder as LoadMoreViewHolder).bind()
        }
    }


    fun setClickListener(block: (CashbackHistory) -> Unit) {
        clickListener = block
    }

    fun loadMore(skipIndex: (Int) -> Unit) {
        load = skipIndex
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CashbackHistory -> 1
            else -> 2
        }
    }

}