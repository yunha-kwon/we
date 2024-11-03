package com.we.presentation.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.we.model.TransactionHistoryData
import com.we.presentation.base.BaseDiffUtil
import com.we.presentation.databinding.ItemAccountCheckBinding
import java.text.NumberFormat
import java.util.Locale

class AccountCheckAdapter :
    ListAdapter<TransactionHistoryData, AccountCheckAdapter.AccountCheckItemViewHolder>(BaseDiffUtil<TransactionHistoryData>()) {

    inner class AccountCheckItemViewHolder(val binding: ItemAccountCheckBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionHistoryData) {
            binding.apply {
                tvAccountCheckPrice.text = item.transactionBalance
                if (item.transactionBalance != "") {
                    tvAccountCheckPrice.text = formatNumberWithCommas(item.transactionBalance.toLong())
                }

                tvAccountCheckLocation.text = item.transactionUserName
                val year = item.transactionDate.substring(0, 4) // "2024"

                val month = item.transactionDate.substring(4, 6) // "10"
                val day = item.transactionDate.substring(6, 8) // "24"

                tvAccountCheckDate.text = "${month}월 ${day}일"
                tvAccountCheckInOut.text = if(item.transactionType == "2") "출금" else "입금"

                val timeLong: Long = 133335
                val timeString = timeLong.toString().padStart(6, '0') // "133335" -> 문자열로 변환 후 "133335"로 보장
                val hour = timeString.substring(0, 2).toInt() // 13
                val minute = timeString.substring(2, 4) // "33"
                val second = timeString.substring(4, 6) // "35"

                val period = if (hour < 12) "오전" else "오후"
                val adjustedHour = if (hour % 12 == 0) 12 else hour % 12 // 12시간제로 변환
                val formattedTime = "$period $adjustedHour:$minute:$second" // "오후 1:33:35"

                tvAccountCheckTime.text = formattedTime


            }
        }
    }

    private fun formatNumberWithCommas(money: Long):String{
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(money)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountCheckItemViewHolder {
        return AccountCheckItemViewHolder(
            ItemAccountCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountCheckItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}