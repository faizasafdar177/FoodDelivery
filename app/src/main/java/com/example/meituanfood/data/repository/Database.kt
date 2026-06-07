package com.example.meituanfood.data.repository

import android.content.Context
import androidx.room.*
import com.example.meituanfood.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCartItemSnapshotList(value: List<CartItemSnapshot>): String = gson.toJson(value)

    @TypeConverter
    fun toCartItemSnapshotList(value: String): List<CartItemSnapshot> {
        val listType = object : TypeToken<List<CartItemSnapshot>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromTrackingEventList(value: List<TrackingEvent>): String = gson.toJson(value)

    @TypeConverter
    fun toTrackingEventList(value: String): List<TrackingEvent> {
        val listType = object : TypeToken<List<TrackingEvent>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String = value.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = OrderStatus.valueOf(value)
}

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem)

    @Update
    suspend fun updateItem(item: CartItem)

    @Delete
    suspend fun deleteItem(item: CartItem)

    @Query("SELECT * FROM cart_items WHERE itemId = :itemId")
    fun getItemById(itemId: String): Flow<CartItem?>

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE restaurantId = :restaurantId")
    fun getCartByRestaurant(restaurantId: String): Flow<List<CartItem>>

    @Query("SELECT SUM(quantity) FROM cart_items WHERE restaurantId = :restaurantId")
    fun getCartCount(restaurantId: String): Flow<Int?>

    @Query("SELECT SUM(price * quantity) FROM cart_items WHERE restaurantId = :restaurantId")
    fun getCartTotal(restaurantId: String): Flow<Double?>

    @Query("SELECT quantity FROM cart_items WHERE itemId = :itemId")
    fun getItemQuantity(itemId: String): Flow<Int?>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun getTotalCount(): Flow<Int?>
}

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: LocalOrder)

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<LocalOrder>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): LocalOrder?
}

@Entity(tableName = "orders")
data class LocalOrder(
    @PrimaryKey val id: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val restaurantImage: String = "",
    val totalAmount: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis(),
    val estimatedTime: String = "",
    val deliveryAddress: String = ""
)

@Database(entities = [CartItem::class, LocalOrder::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MeituanDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: MeituanDatabase? = null

        fun getDatabase(context: Context): MeituanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeituanDatabase::class.java,
                    "meituan_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}