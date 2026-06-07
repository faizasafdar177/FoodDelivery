package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.MenuItem
import com.example.meituanfood.databinding.ItemGroceryProductBinding

class GroceryItemAdapter(private val onClick: (MenuItem) -> Unit) :
    ListAdapter<MenuItem, GroceryItemAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemGroceryProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGroceryProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            tvName.text = item.name
            tvPrice.text = "¥${item.price}"
            Glide.with(ivProduct.context).load(item.imageUrl).into(ivProduct)
            root.setOnClickListener { onClick(item) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean = oldItem == newItem
    }
}
