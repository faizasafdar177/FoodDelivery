package com.example.meituanfood.ui.orders

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meituanfood.adapter.TrackingAdapter
import com.example.meituanfood.data.model.Order
import com.example.meituanfood.data.model.OrderStatus
import com.example.meituanfood.databinding.ActivityOrderTrackingBinding
import com.google.firebase.firestore.FirebaseFirestore

class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderTrackingBinding
    private lateinit var trackingAdapter: TrackingAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val orderId = intent.getStringExtra("order_id") ?: return
        
        setupTrackingList()
        observeOrder(orderId)
    }

    private fun observeOrder(orderId: String) {
        // Real-time tracking using Firebase Firestore
        db.collection("orders").document(orderId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                
                val order = snapshot.toObject(Order::class.java)
                if (order != null) {
                    setupUI(order)
                }
            }
    }

    private fun setupTrackingList() {
        trackingAdapter = TrackingAdapter()
        binding.rvTrackingEvents.apply {
            layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
            adapter = trackingAdapter
        }
    }

    private fun setupUI(order: Order) {
        binding.tvStatus.text = when (order.status) {
            OrderStatus.PENDING -> "Waiting for confirmation"
            OrderStatus.ACCEPTED -> "Order accepted"
            OrderStatus.PREPARING -> "Preparing your food"
            OrderStatus.DELIVERING -> "Rider is delivering"
            OrderStatus.DELIVERED -> "Enjoy your meal!"
            OrderStatus.CANCELLED -> "Order cancelled"
        }

        binding.tvEstimatedTime.text = "Estimated arrival: ${order.estimatedTime}"

        if (!order.riderPhone.isNullOrEmpty()) {
            binding.btnCallRider.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${order.riderPhone}")
                }
                startActivity(intent)
            }
            binding.btnMessageRider.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:${order.riderPhone}")
                }
                startActivity(intent)
            }
        }

        // In a real app, trackingEvents would be a subcollection. 
        // For UI purposes, we use the list if provided in the model snapshot.
        // trackingAdapter.submitList(order.trackingEvents.reversed())

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
