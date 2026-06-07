package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.CartItem
import com.example.meituanfood.databinding.ItemCartBinding

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            Glide.with(binding.root)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.ivItemImage)
            binding.tvItemName.text = item.itemName.ifEmpty { item.name }
            binding.tvItemPrice.text = "$" + String.format("%.2f", item.itemPrice.takeIf { it > 0 } ?: item.price)
            binding.tvQuantity.text = item.quantity.toString()
            binding.tvRestaurantName.text = item.restaurantName
            binding.tvTotalPrice.text = "Subtotal: $" + String.format("%.2f", (item.itemPrice.takeIf { it > 0 } ?: item.price) * item.quantity)
            binding.btnIncrease.setOnClickListener { onIncrease(item) }
            binding.btnRemove.setOnClickListener { onDecrease(item) }
            binding.btnDelete.setOnClickListener { onRemove(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.itemId == newItem.itemId
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem == newItem
    }
}