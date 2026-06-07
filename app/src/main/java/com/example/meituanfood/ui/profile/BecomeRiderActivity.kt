package com.example.meituanfood.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meituanfood.databinding.ActivityBecomeRiderBinding

class BecomeRiderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBecomeRiderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBecomeRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val vehicle = binding.etVehicle.text.toString().trim()

            binding.tilName.error = null
            binding.tilPhone.error = null
            binding.tilVehicle.error = null

            var valid = true

            if (name.isEmpty()) {
                binding.tilName.error = "Please enter your full name"
                valid = false
            } else if (name.length < 3) {
                binding.tilName.error = "Name must be at least 3 characters"
                valid = false
            }

            if (phone.isEmpty()) {
                binding.tilPhone.error = "Please enter your phone number"
                valid = false
            } else if (phone.length < 10) {
                binding.tilPhone.error = "Enter a valid phone number"
                valid = false
            }

            if (vehicle.isEmpty()) {
                binding.tilVehicle.error = "Please enter your vehicle number"
                valid = false
            } else if (vehicle.length < 4) {
                binding.tilVehicle.error = "Enter a valid vehicle number"
                valid = false
            }

            if (valid) {
                binding.btnRegister.visibility = View.GONE
                binding.layoutOnline.visibility = View.VISIBLE
                Toast.makeText(this, "Welcome $name! You are now online.", Toast.LENGTH_LONG).show()
            }
        }
    }
}