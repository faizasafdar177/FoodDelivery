package com.example.meituanfood.data.repository

import com.example.meituanfood.data.model.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    fun getCartByRestaurant(restaurantId: String): Flow<List<CartItem>> {
        return cartDao.getCartByRestaurant(restaurantId)
    }

    fun getCartCount(restaurantId: String): Flow<Int?> {
        return cartDao.getCartCount(restaurantId)
    }

    fun getCartTotal(restaurantId: String): Flow<Double?> {
        return cartDao.getCartTotal(restaurantId)
    }

    fun getItemQuantity(itemId: String): Flow<Int?> {
        return cartDao.getItemQuantity(itemId)
    }

    fun getItemById(itemId: String): Flow<CartItem?> {
        return cartDao.getItemById(itemId)
    }

    fun getTotalCount(): Flow<Int?> {
        return cartDao.getTotalCount()
    }

    suspend fun addItem(item: CartItem) {
        cartDao.insertItem(item)
    }

    suspend fun updateItem(item: CartItem) {
        cartDao.updateItem(item)
    }

    suspend fun removeItem(item: CartItem) {
        cartDao.deleteItem(item)
    }

    suspend fun clearAllCart() {
        cartDao.clearCart()
    }
}
