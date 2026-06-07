package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.Review
import com.example.meituanfood.databinding.ItemReviewBinding
import com.google.android.material.chip.Chip

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.tvUserName.text = review.userName
            binding.tvReviewDate.text = review.date
            binding.tvReviewContent.text = review.content
            binding.ratingBar.rating = review.rating

            Glide.with(binding.root)
                .load(review.userAvatar)
                .placeholder(com.example.meituanfood.R.drawable.ic_launcher_background)
                .into(binding.ivUserAvatar)

            binding.cgTags.removeAllViews()
            review.tags.forEach { tag ->
                val chip = Chip(binding.root.context)
                chip.text = tag
                chip.textSize = 10f
                binding.cgTags.addView(chip)
            }

            binding.rvReviewImages.visibility = if (review.images.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
    }
}