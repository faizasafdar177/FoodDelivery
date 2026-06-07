package com.example.meituanfood.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val fcmToken: String = "",
    val defaultAddressId: String = ""
)

data class DeliveryAddress(
    val id: String = "",
    val label: String = "",
    val fullAddress: String = "",
    val detail: String = "",
    val contactName: String = "",
    val contactPhone: String = "",
    val isDefault: Boolean = false
)

data class Restaurant(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val rating: Float = 0.0f,
    val reviewCount: Int = 0,
    val deliveryTime: Int = 0,
    val deliveryFee: Double = 0.0,
    val minOrder: Double = 0.0,
    val distance: String = "",
    val categories: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isPromoted: Boolean = false,
    val discount: String = "",
    val monthlyOrders: Int = 0,
    val isOpen: Boolean = true,
    val address: String = "",
    val notice: String = ""
)

data class MenuCategory(
    val id: String = "",
    val name: String = "",
    val restaurantId: String = "",
    val items: List<MenuItem> = emptyList()
)

data class MenuItem(
    val id: String = "",
    val restaurantId: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val monthlyOrders: Int = 0,
    val rating: Float = 5.0f,
    val category: String = "Main",
    val categoryId: String = "",
    val isRecommended: Boolean = false,
    val isNew: Boolean = false,
    val customizations: List<String> = emptyList()
)

data class ComboDeal(
    val id: String = "",
    val restaurantId: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val imageUrl: String = ""
)

data class Order(
    val id: String = "",
    val userId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val restaurantImage: String = "",
    val items: List<CartItemSnapshot> = emptyList(),
    val totalAmount: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: String = "",
    val estimatedTime: String = "",
    val deliveryAddress: String = "",
    val riderId: String? = null,
    val riderName: String? = null,
    val riderPhone: String? = null
)

data class CartItemSnapshot(
    val itemId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val itemId: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val restaurantId: String = "",
    val restaurantName: String = "",
    val itemName: String = "",
    val itemPrice: Double = 0.0,
    val customizations: String = ""
)

data class Cart(
    val userId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0
)

data class Rider(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val vehicleNumber: String = "",
    val activeOrderId: String? = null,
    val isOnline: Boolean = false
)

data class TrackingEvent(
    val status: OrderStatus = OrderStatus.PENDING,
    val time: String = "",
    val description: String = ""
)

enum class OrderStatus {
    PENDING, ACCEPTED, PREPARING, DELIVERING, DELIVERED, CANCELLED
}

data class Review(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userAvatar: String = "",
    val restaurantId: String = "",
    val orderId: String = "",
    val rating: Float = 5.0f,
    val content: String = "",
    val date: String = "",
    val tags: List<String> = emptyList(),
    val images: List<String> = emptyList()
)

data class FoodCategory(
    val id: String = "",
    val name: String = "",
    val iconUrl: String = "",
    val color: String = "#FF4D00"
)

data class Banner(
    val id: String = "",
    val imageUrl: String = "",
    val title: String = ""
)