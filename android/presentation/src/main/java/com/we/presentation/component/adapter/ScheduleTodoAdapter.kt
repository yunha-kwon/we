package com.we.presentation.component.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.we.model.ScheduleData
import com.we.presentation.base.BaseDiffUtil
import com.we.presentation.databinding.ItemScheduleTodoBinding
import timber.log.Timber

class ScheduleTodoAdapter : ListAdapter<ScheduleData, ScheduleTodoAdapter.ScheduleTodoViewHolder>(
    BaseDiffUtil<ScheduleData>()
) {

    private var onItemClickListener: ((ScheduleData) -> Unit)? = null
    private var onMenuClickListener: ((ScheduleData, View) -> Unit)? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleTodoViewHolder {
        val binding =
            ItemScheduleTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleTodoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleTodoViewHolder,
        position: Int
    ) {
        holder.bind(getItem(holder.adapterPosition))
        holder.binding.root.setOnClickListener {
            onItemClickListener?.let {
                val isSelected = holder.binding.ivCheck.isSelected
                it(getItem(holder.adapterPosition))
                holder.binding.ivCheck.isSelected = !isSelected
            }
        }
        holder.binding.ivMore.setOnClickListener { view ->
            onMenuClickListener?.let {
                it(getItem(holder.adapterPosition), view)
            }
        }
    }

    class ScheduleTodoViewHolder(
        val binding: ItemScheduleTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scheduleData: ScheduleData) {
            binding.apply {
                Timber.tag("할일").d("$scheduleData")
                this.ivCheck.isSelected = scheduleData.done
                this.scheduleData = scheduleData

            }
        }
    }

    fun setOnItemClickListener(listener: (ScheduleData) -> Unit) {
        this.onItemClickListener = listener
    }

    fun setOnMenuClickListener(listener: (ScheduleData, View) -> Unit) {
        this.onMenuClickListener = listener
    }
}