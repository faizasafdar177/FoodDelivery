package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.Restaurant
import com.example.meituanfood.databinding.ItemRestaurantBinding

class RestaurantAdapter(
    private val showTag: Boolean = false,
    private val onClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RestaurantViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            Glide.with(binding.root)
                .load(restaurant.imageUrl)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivRestaurant)

            binding.tvRestaurantName.text = restaurant.name
            binding.tvRating.text = restaurant.rating.toString()
            binding.tvDeliveryTime.text = "${restaurant.deliveryTime} min"
            binding.tvDeliveryFee.text = if (restaurant.deliveryFee == 0.0) "Free delivery" else "Fee $${restaurant.deliveryFee.toInt()}"
            binding.tvMinOrder.text = "Min $${restaurant.minOrder.toInt()}"

            if (showTag && restaurant.categories.isNotEmpty()) {
                val tag = restaurant.categories.find { it != "Food" && it != "Grocery" } ?: restaurant.categories[0]
                binding.tvCategoryTag.text = tag
                binding.tvCategoryTag.visibility = View.VISIBLE
            } else {
                binding.tvCategoryTag.visibility = View.GONE
            }

            binding.root.setOnClickListener { onClick(restaurant) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) = oldItem == newItem
    }
}