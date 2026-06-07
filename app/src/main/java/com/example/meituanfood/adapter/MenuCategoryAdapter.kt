package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.meituanfood.R
import com.example.meituanfood.databinding.ItemMenuCategoryBinding

class MenuCategoryAdapter(
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder>() {

    private var items = listOf<String>()
    private var selectedIndex = 0

    fun submitList(list: List<String>) {
        items = list
        notifyDataSetChanged()
    }

    fun setSelectedIndex(index: Int) {
        val old = selectedIndex
        selectedIndex = index
        notifyItemChanged(old)
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedIndex)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemMenuCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, isSelected: Boolean) {
            binding.tvCategoryName.text = name
            if (isSelected) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.white)
                )
                binding.tvCategoryName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.meituan_orange)
                )
                binding.viewIndicator.visibility = android.view.View.VISIBLE
            } else {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.bg_grey)
                )
                binding.tvCategoryName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_primary)
                )
                binding.viewIndicator.visibility = android.view.View.GONE
            }
            binding.root.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }
}
