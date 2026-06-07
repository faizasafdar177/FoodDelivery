package com.example.meituanfood.ui.orders

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meituanfood.databinding.ActivityReviewOrderBinding

class ReviewOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("order_id") ?: ""

        binding.btnSubmitReview.setOnClickListener {
            val foodRating = binding.rbFood.rating
            val deliveryRating = binding.rbDelivery.rating
            val comment = binding.etComment.text.toString()

            if (foodRating == 0f || deliveryRating == 0f) {
                Toast.makeText(this, "Please rate both food and delivery", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app, send this to the server/database
            Toast.makeText(this, "Review submitted! Thank you.", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.btnUploadPhoto.setOnClickListener {
            Toast.makeText(this, "Gallery opened", Toast.LENGTH_SHORT).show()
        }
    }
}
