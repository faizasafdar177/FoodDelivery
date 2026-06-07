package com.example.meituanfood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.meituanfood.data.model.Order
import com.example.meituanfood.data.model.OrderStatus
import com.example.meituanfood.data.repository.LocalOrder
import com.example.meituanfood.data.repository.MeituanDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrderViewModel(app: Application) : AndroidViewModel(app) {

    private val db = MeituanDatabase.getDatabase(app)
    private val orderDao = db.orderDao()

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedTab = MutableLiveData(0)
    val selectedTab: LiveData<Int> = _selectedTab

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            orderDao.getAllOrders().collect { list ->
                _orders.value = applyTabFilter(list.map { it.toOrder() }, _selectedTab.value ?: 0)
                _isLoading.value = false
            }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
        viewModelScope.launch {
            val all = orderDao.getAllOrders().first().map { it.toOrder() }
            _orders.value = applyTabFilter(all, index)
        }
    }

    private fun LocalOrder.toOrder() = Order(
        id = id,
        restaurantId = restaurantId,
        restaurantName = restaurantName,
        restaurantImage = restaurantImage,
        totalAmount = totalAmount,
        deliveryFee = deliveryFee,
        status = try { OrderStatus.valueOf(status) } catch (e: Exception) { OrderStatus.PENDING },
        createdAt = createdAt.toString(),
        estimatedTime = estimatedTime,
        deliveryAddress = deliveryAddress
    )

    private fun applyTabFilter(all: List<Order>, index: Int): List<Order> {
        return when (index) {
            0 -> all
            1 -> all.filter { it.status == OrderStatus.PENDING || it.status == OrderStatus.ACCEPTED }
            2 -> all.filter { it.status == OrderStatus.PREPARING || it.status == OrderStatus.DELIVERING }
            3 -> all.filter { it.status == OrderStatus.DELIVERED }
            4 -> all.filter { it.status == OrderStatus.CANCELLED }
            else -> all
        }
    }

    fun getStatusText(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "Pending"
            OrderStatus.ACCEPTED -> "Accepted"
            OrderStatus.PREPARING -> "Preparing"
            OrderStatus.DELIVERING -> "Out for Delivery"
            OrderStatus.DELIVERED -> "Delivered"
            OrderStatus.CANCELLED -> "Cancelled"
        }
    }

    fun getStatusColor(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "#FF9800"
            OrderStatus.ACCEPTED -> "#2196F3"
            OrderStatus.PREPARING -> "#9C27B0"
            OrderStatus.DELIVERING -> "#FF6D00"
            OrderStatus.DELIVERED -> "#4CAF50"
            OrderStatus.CANCELLED -> "#9E9E9E"
        }
    }
}