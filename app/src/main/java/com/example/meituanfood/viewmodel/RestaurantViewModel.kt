package com.example.meituanfood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.meituanfood.data.model.*
import com.example.meituanfood.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RestaurantViewModel(app: Application) : AndroidViewModel(app) {

    private val db = MeituanDatabase.getDatabase(app)
    private val cartRepo = CartRepository(db.cartDao())

    var restaurantId = ""

    private val _restaurant = MutableLiveData<Restaurant?>()
    val restaurant: LiveData<Restaurant?> = _restaurant

    private val _menuCategories = MutableLiveData<List<MenuCategory>>()
    val menuCategories: LiveData<List<MenuCategory>> = _menuCategories

    private val _cartCount = MutableLiveData(0)
    val cartCount: LiveData<Int> = _cartCount

    private val _cartTotal = MutableLiveData(0.0)
    val cartTotal: LiveData<Double> = _cartTotal

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun load(id: String) {
        restaurantId = id
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(400)
            _restaurant.value = MockData.getRestaurants().find { it.id == id }
            val items = MockData.getMenuItems(id)
            _menuCategories.value = listOf(MenuCategory("1", "Menu", id, items))
            _isLoading.value = false
        }
        viewModelScope.launch {
            cartRepo.getCartCount(id).collect { _cartCount.value = it ?: 0 }
        }
        viewModelScope.launch {
            cartRepo.getCartTotal(id).collect { _cartTotal.value = it ?: 0.0 }
        }
    }

    fun addToCart(item: MenuItem, customizations: String = "") {
        viewModelScope.launch {
            val r = _restaurant.value ?: return@launch
            val cartItemId = if (customizations.isEmpty()) item.id else "${item.id}_${customizations.hashCode()}"
            val existingItem = cartRepo.getItemById(cartItemId).first()
            val qty = existingItem?.quantity ?: 0
            cartRepo.addItem(
                CartItem(
                    itemId = cartItemId,
                    restaurantId = restaurantId,
                    restaurantName = r.name,
                    name = item.name,
                    itemName = if (customizations.isEmpty()) item.name else "${item.name} ($customizations)",
                    imageUrl = item.imageUrl,
                    price = item.price,
                    itemPrice = item.price,
                    quantity = qty + 1,
                    customizations = customizations
                )
            )
        }
    }

    fun removeFromCart(item: MenuItem) {
        viewModelScope.launch {
            val existingItem = cartRepo.getItemById(item.id).first() ?: return@launch
            if (existingItem.quantity > 1) {
                cartRepo.updateItem(existingItem.copy(quantity = existingItem.quantity - 1))
            } else {
                cartRepo.removeItem(existingItem)
            }
        }
    }

    fun getQtyFlow(itemId: String): Flow<Int?> = cartRepo.getItemQuantity(itemId)
}