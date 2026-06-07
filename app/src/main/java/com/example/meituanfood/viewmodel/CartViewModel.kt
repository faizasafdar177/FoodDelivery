package com.example.meituanfood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.meituanfood.data.model.CartItem
import com.example.meituanfood.data.repository.*
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {

    private val db = MeituanDatabase.getDatabase(app)
    private val repo = CartRepository(db.cartDao())

    val cartItems: LiveData<List<CartItem>> = repo.getAllCartItems().asLiveData()

    private val _orderPlaced = MutableLiveData(false)
    val orderPlaced: LiveData<Boolean> = _orderPlaced

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun increase(item: CartItem) {
        viewModelScope.launch {
            repo.updateItem(item.copy(quantity = item.quantity + 1))
        }
    }

    fun decrease(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                repo.updateItem(item.copy(quantity = item.quantity - 1))
            } else {
                repo.removeItem(item)
            }
        }
    }

    fun remove(item: CartItem) {
        viewModelScope.launch { repo.removeItem(item) }
    }

    fun clearCart() {
        viewModelScope.launch { repo.clearAllCart() }
    }

    fun placeOrder() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1500)
            repo.clearAllCart()
            _isLoading.value = false
            _orderPlaced.value = true
        }
    }

    fun resetOrderPlaced() { _orderPlaced.value = false }

    fun getSubTotal(items: List<CartItem>): Double {
        return items.sumOf { (it.itemPrice.takeIf { p -> p > 0 } ?: it.price) * it.quantity }
    }

    fun getDeliveryFee(total: Double): Double = if (total >= 30) 0.0 else 5.0

    fun getTotal(items: List<CartItem>): Double {
        val sub = getSubTotal(items)
        return sub + getDeliveryFee(sub)
    }

    fun getTotalCount(items: List<CartItem>): Int = items.sumOf { it.quantity }
}