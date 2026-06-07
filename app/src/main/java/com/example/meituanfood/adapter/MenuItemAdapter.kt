package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meituanfood.data.model.MenuItem
import com.example.meituanfood.databinding.ItemMenuItemBinding
import kotlinx.coroutines.flow.Flow

class MenuItemAdapter(
    private val onAdd: (MenuItem) -> Unit,
    private val onRemove: (MenuItem) -> Unit,
    private val onShowCustomization: (MenuItem) -> Unit,
    private val getQty: (String) -> Flow<Int?>
) : ListAdapter<MenuItem, MenuItemAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            Glide.with(binding.root).load(item.imageUrl).centerCrop().into(binding.ivMenuItem)
            binding.tvItemName.text = item.name
            binding.tvItemDesc.text = item.description
            binding.tvItemPrice.text = "$${String.format("%.2f", item.price)}"

            binding.btnAdd.setOnClickListener {
                if (item.customizations.isNotEmpty()) onShowCustomization(item)
                else onAdd(item)
            }
            binding.btnRemove.setOnClickListener { onRemove(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem) = oldItem == newItem
    }
}