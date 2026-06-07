package com.example.meituanfood.data.repository

import com.example.meituanfood.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    // USERS
    suspend fun saveUserProfile(profile: UserProfile) {
        db.collection("users").document(profile.id).set(profile, SetOptions.merge()).await()
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        return db.collection("users").document(userId).get().await().toObject(UserProfile::class.java)
    }

    // RESTAURANTS
    suspend fun getRestaurants(): List<Restaurant> {
        return db.collection("restaurants").get().await().toObjects(Restaurant::class.java)
    }

    suspend fun getMenuItems(restaurantId: String): List<MenuItem> {
        return db.collection("restaurants").document(restaurantId)
            .collection("menuItems").get().await().toObjects(MenuItem::class.java)
    }

    // ORDERS
    suspend fun placeOrder(order: Order) {
        db.collection("orders").document(order.id).set(order).await()
    }

    fun observeOrder(orderId: String, onUpdate: (Order) -> Unit) {
        db.collection("orders").document(orderId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                snapshot?.toObject(Order::class.java)?.let(onUpdate)
            }
    }

    // RIDERS
    suspend fun updateRiderLocation(riderId: String, location: GeoPoint) {
        db.collection("riders").document(riderId)
            .update("location", location).await()
    }

    fun observeRiderLocation(riderId: String, onUpdate: (GeoPoint) -> Unit) {
        db.collection("riders").document(riderId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                snapshot?.getGeoPoint("location")?.let(onUpdate)
            }
    }

    // CARTS
    suspend fun saveCart(cart: Cart) {
        db.collection("carts").document(cart.userId).set(cart).await()
    }

    suspend fun getCart(userId: String): Cart? {
        return db.collection("carts").document(userId).get().await().toObject(Cart::class.java)
    }
    
    suspend fun clearCart(userId: String) {
        db.collection("carts").document(userId).delete().await()
    }
}
