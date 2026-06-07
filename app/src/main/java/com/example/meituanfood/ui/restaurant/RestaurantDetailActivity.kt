package com.example.meituanfood.ui.restaurant

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.meituanfood.adapter.MenuItemAdapter
import com.example.meituanfood.data.model.MenuItem
import com.example.meituanfood.databinding.ActivityRestaurantDetailBinding
import com.example.meituanfood.viewmodel.RestaurantViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private val viewModel: RestaurantViewModel by viewModels()
    private lateinit var menuItemAdapter: MenuItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantId = intent.getStringExtra("restaurant_id") ?: return

        setupAdapters()
        setupObservers()
        setupClickListeners()
        setupAppBarBehavior()

        viewModel.load(restaurantId)
    }

    private fun setupAdapters() {
        menuItemAdapter = MenuItemAdapter(
            onAdd = { item -> viewModel.addToCart(item) },
            onRemove = { item -> viewModel.removeFromCart(item) },
            onShowCustomization = { item -> showCustomizationDialog(item) },
            getQty = { itemId -> viewModel.getQtyFlow(itemId) }
        )

        binding.rvMenuItems.apply {
            layoutManager = LinearLayoutManager(this@RestaurantDetailActivity)
            adapter = menuItemAdapter
        }
    }

    private fun setupAppBarBehavior() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percentage = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            binding.tvToolbarTitle.alpha = percentage
            val iconColor = if (percentage > 0.5)
                getColor(com.example.meituanfood.R.color.text_primary)
            else
                getColor(com.example.meituanfood.R.color.white)
            binding.toolbar.navigationIcon?.setTint(iconColor)
        })
    }

    private fun showCustomizationDialog(item: MenuItem) {
        CustomizationBottomSheet(item) { customizedItem, selection ->
            viewModel.addToCart(customizedItem, selection)
        }.show(supportFragmentManager, CustomizationBottomSheet.TAG)
    }

    private fun setupObservers() {
        viewModel.restaurant.observe(this) { restaurant ->
            restaurant?.let {
                binding.tvRestaurantName.text = it.name
                binding.tvToolbarTitle.text = it.name
                binding.tvRating.text = it.rating.toString()
                binding.tvDeliveryTime.text = "${it.deliveryTime} min"
                Glide.with(this).load(it.imageUrl).into(binding.ivRestaurantHeader)
            }
        }

        viewModel.menuCategories.observe(this) { categories ->
            val allItems = categories.flatMap { it.items }
            menuItemAdapter.submitList(allItems)
        }

        viewModel.cartCount.observe(this) { count ->
            if (count != null && count > 0) {
                binding.cartBar.visibility = View.VISIBLE
                binding.tvCartCount.text = count.toString()
            } else {
                binding.cartBar.visibility = View.GONE
            }
        }

        viewModel.cartTotal.observe(this) { total ->
            binding.tvCartTotal.text = "$${String.format("%.2f", total)}"
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnGoToCart.setOnClickListener {
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}