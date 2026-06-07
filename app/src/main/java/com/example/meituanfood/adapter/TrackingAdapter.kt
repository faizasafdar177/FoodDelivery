package com.example.meituanfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meituanfood.data.model.TrackingEvent
import com.example.meituanfood.databinding.ItemTrackingEventBinding

class TrackingAdapter : ListAdapter<TrackingEvent, TrackingAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackingEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position == 0, position == itemCount - 1)
    }

    inner class ViewHolder(private val binding: ItemTrackingEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: TrackingEvent, isFirst: Boolean, isLast: Boolean) {
            binding.tvEventTitle.text = event.status.name
            binding.tvEventTime.text = event.time
            binding.tvEventDesc.text = event.description
            binding.viewTopLine.visibility = if (isFirst) View.INVISIBLE else View.VISIBLE
            binding.viewBottomLine.visibility = if (isLast) View.INVISIBLE else View.VISIBLE
            if (isFirst) {
                binding.dot.setBackgroundResource(com.example.meituanfood.R.drawable.bg_orange_circle)
                binding.tvEventTitle.setTextColor(binding.root.context.getColor(com.example.meituanfood.R.color.meituan_orange))
            } else {
                binding.dot.setBackgroundResource(com.example.meituanfood.R.drawable.bg_grey_circle)
                binding.tvEventTitle.setTextColor(binding.root.context.getColor(com.example.meituanfood.R.color.text_primary))
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TrackingEvent>() {
        override fun areItemsTheSame(oldItem: TrackingEvent, newItem: TrackingEvent) = oldItem.time == newItem.time
        override fun areContentsTheSame(oldItem: TrackingEvent, newItem: TrackingEvent) = oldItem == newItem
    }
}