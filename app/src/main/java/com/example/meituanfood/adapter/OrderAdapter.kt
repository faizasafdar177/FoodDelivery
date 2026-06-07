package com.example.meituanfood.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.Order
import com.example.meituanfood.data.model.OrderStatus
import com.example.meituanfood.databinding.ItemOrderBinding

class OrderAdapter(
    private val getStatusText: (OrderStatus) -> String,
    private val getStatusColor: (OrderStatus) -> String,
    private val onOrderClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            Glide.with(binding.root)
                .load(order.restaurantImage)
                .centerCrop()
                .into(binding.ivRestaurantImage)
            binding.tvRestaurantName.text = order.restaurantName
            binding.tvOrderStatus.text = getStatusText(order.status)
            binding.tvOrderStatus.setTextColor(Color.parseColor(getStatusColor(order.status)))
            binding.tvOrderDate.text = order.createdAt
            
            binding.tvOrderTotal.text = "¥" + String.format("%.2f", order.totalAmount)
            binding.tvOrderItems.text = order.items.joinToString("、") { it.name + "x" + it.quantity }
            val itemCount = order.items.sumOf { it.quantity }
            binding.tvItemCount.text = "Total " + itemCount + " items"

            binding.root.setOnClickListener { onOrderClick(order) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
    }
}
