package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meituanfood.data.model.FoodCategory
import com.example.meituanfood.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onClick: (FoodCategory) -> Unit
) : ListAdapter<FoodCategory, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    private var selectedCategoryName: String? = "All"

    fun setSelectedCategory(name: String?) {
        selectedCategoryName = name ?: "All"
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: FoodCategory) {
            binding.tvCategoryName.text = category.name
            val isSelected = category.name == selectedCategoryName

            binding.tvCategoryIcon.text = when (category.name) {
                "All" -> "🍽️"
                "Burgers" -> "🍔"
                "Pizza" -> "🍕"
                "Sushi" -> "🍱"
                "Desi" -> "🍛"
                "Dessert" -> "🍰"
                "Fast Food" -> "🌮"
                "Coffee" -> "☕"
                "Drinks" -> "🥤"
                "Chicken" -> "🍗"
                "Noodles" -> "🍜"
                "Salad" -> "🥗"
                else -> "🍴"
            }

            val bgColor = if (isSelected)
                android.graphics.Color.parseColor("#FF6D00")
            else
                android.graphics.Color.parseColor("#F3F3F3")

            binding.cardCategory.setCardBackgroundColor(bgColor)

            val textColor = if (isSelected)
                android.graphics.Color.WHITE
            else
                android.graphics.Color.parseColor("#666666")

            binding.tvCategoryName.setTextColor(textColor)

            binding.root.setOnClickListener { onClick(category) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FoodCategory>() {
        override fun areItemsTheSame(oldItem: FoodCategory, newItem: FoodCategory) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FoodCategory, newItem: FoodCategory) = oldItem == newItem
    }
}