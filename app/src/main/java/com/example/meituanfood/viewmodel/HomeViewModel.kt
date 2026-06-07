package com.example.meituanfood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.meituanfood.data.model.*
import com.example.meituanfood.data.repository.*
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val db = MeituanDatabase.getDatabase(app)
    private val cartRepo = CartRepository(db.cartDao())

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    private val _categories = MutableLiveData<List<FoodCategory>>()
    val categories: LiveData<List<FoodCategory>> = _categories

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _address = MutableLiveData("Gulberg III, Lahore")
    val address: LiveData<String> = _address

    private val _selectedType = MutableLiveData<String?>("All")
    val selectedType: LiveData<String?> = _selectedType

    private val _activeFilters = MutableLiveData<Set<String>>(emptySet())
    val activeFilters: LiveData<Set<String>> = _activeFilters

    private val _currentSort = MutableLiveData<String>("Default")
    val currentSort: LiveData<String> = _currentSort

    val cartCount: LiveData<Int?> = cartRepo.getTotalCount().asLiveData()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(800)
            _banners.value = MockData.getBanners()
            _categories.value = MockData.getCategories()
            applyFiltersAndSort()
            _isLoading.value = false
        }
    }

    fun setAddress(addr: String) { _address.value = addr }

    fun filterByCategory(categoryName: String) {
        _selectedType.value = categoryName
        applyFiltersAndSort()
    }

    fun toggleFilter(filter: String) {
        val current = _activeFilters.value ?: emptySet()
        _activeFilters.value = if (current.contains(filter)) current - filter else current + filter
        applyFiltersAndSort()
    }

    fun setSort(sort: String) {
        _currentSort.value = sort
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        var list = MockData.getRestaurants()
        val type = _selectedType.value
        val filters = _activeFilters.value ?: emptySet()
        val sort = _currentSort.value

        if (type != null && type != "All") {
            list = list.filter { it.categories.any { c -> c.equals(type, ignoreCase = true) } }
        }
        if (filters.contains("Free Delivery")) list = list.filter { it.deliveryFee == 0.0 }
        if (filters.contains("Deal")) list = list.filter { it.discount.isNotEmpty() }
        if (filters.contains("Rating 4.5+")) list = list.filter { it.rating >= 4.5f }

        list = when (sort) {
            "Rating" -> list.sortedByDescending { it.rating }
            "Delivery Time" -> list.sortedBy { it.deliveryTime }
            else -> list
        }
        _restaurants.value = list
    }
}