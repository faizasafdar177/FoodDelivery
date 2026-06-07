package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meituanfood.data.model.ComboDeal
import com.example.meituanfood.databinding.ItemComboDealBinding

class ComboDealAdapter(
    private val onAdd: (ComboDeal) -> Unit
) : ListAdapter<ComboDeal, ComboDealAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComboDealBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemComboDealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(deal: ComboDeal) {
            binding.tvComboTitle.text = deal.title
            binding.tvComboDesc.text = deal.description
            binding.tvComboPrice.text = "¥${deal.price}"
            binding.root.setOnClickListener { onAdd(deal) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ComboDeal>() {
        override fun areItemsTheSame(oldItem: ComboDeal, newItem: ComboDeal) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ComboDeal, newItem: ComboDeal) = oldItem == newItem
    }
}
