package com.example.meituanfood.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meituanfood.R
import com.example.meituanfood.adapter.*
import com.example.meituanfood.databinding.FragmentHomeBinding
import com.example.meituanfood.ui.restaurant.RestaurantDetailActivity
import com.example.meituanfood.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var featuredAdapter: RestaurantAdapter
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupObservers()
        setupClickListeners()
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            viewModel.filterByCategory(category.name)
        }
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        featuredAdapter = RestaurantAdapter(showTag = false) { restaurant ->
            openRestaurantDetail(restaurant.id)
        }
        binding.rvFeatured.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredAdapter
        }

        restaurantAdapter = RestaurantAdapter(showTag = true) { restaurant ->
            openRestaurantDetail(restaurant.id)
        }
        binding.rvRestaurants.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = restaurantAdapter
        }
    }

    private fun openRestaurantDetail(id: String) {
        val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
        intent.putExtra("restaurant_id", id)
        startActivity(intent)
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.submitList(categories)
        }

        viewModel.selectedType.observe(viewLifecycleOwner) { type ->
            categoryAdapter.setSelectedCategory(type)
            updateFeaturedList()
        }

        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            restaurantAdapter.submitList(restaurants)
            updateFeaturedList()
        }

        viewModel.address.observe(viewLifecycleOwner) { address ->
            binding.tvAddress.text = address
        }
    }

    private fun updateFeaturedList() {
        val restaurants = viewModel.restaurants.value ?: return
        val selectedType = viewModel.selectedType.value
        
        if (selectedType == "All") {
            // In "All" tab, show all restaurants in featured
            featuredAdapter.submitList(restaurants)
        } else {
            // In other tabs, show more restaurants (e.g., 6)
            featuredAdapter.submitList(restaurants.take(6))
        }
    }

    private fun setupClickListeners() {
        binding.btnSeeAllFeatured.setOnClickListener {
            // "See all" shows all restaurants in the featured list
            val allRestaurants = viewModel.restaurants.value ?: emptyList()
            featuredAdapter.submitList(allRestaurants)
            Toast.makeText(context, "Showing all restaurants", Toast.LENGTH_SHORT).show()
        }

        binding.layoutAddressTop.setOnClickListener {
            // Logic to open Address Picker
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
