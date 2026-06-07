package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.Banner
import com.example.meituanfood.databinding.ItemBannerBinding

class BannerAdapter : ListAdapter<Banner, BannerAdapter.BannerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: Banner) {
            Glide.with(binding.root)
                .load(banner.imageUrl)
                .centerCrop()
                .into(binding.ivBanner)
            binding.tvBannerTitle.text = banner.title
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Banner, newItem: Banner) = oldItem == newItem
    }
}
