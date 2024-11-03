package com.we.presentation.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.we.presentation.databinding.ItemBannerBinding

class HomeViewPagerBannerAdapter(val item: List<Int>) : RecyclerView.Adapter<HomeViewPagerBannerAdapter.HomeViewPagerBannerViewHolder>() {
    inner class HomeViewPagerBannerViewHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Int){
            binding.apply {
                ivBanner.setImageResource(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewPagerBannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBannerBinding.inflate(inflater, parent, false)
        return HomeViewPagerBannerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: HomeViewPagerBannerViewHolder, position: Int) {
        holder.bind(item[position])
    }
}