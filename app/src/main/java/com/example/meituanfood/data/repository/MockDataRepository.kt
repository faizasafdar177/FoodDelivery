package com.example.meituanfood.data.repository

import com.example.meituanfood.data.model.*

object MockData {

    private val foodImages = listOf(
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400",
        "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400",
        "https://images.unsplash.com/photo-1553621042-f6e147245754?w=400",
        "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400",
        "https://images.unsplash.com/photo-1432139555190-58524dae6a55?w=400",
        "https://images.unsplash.com/photo-1601924582970-9238bcb495d9?w=400",
        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400",
        "https://images.unsplash.com/photo-1484723091739-30a097e8f929?w=400",
        "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=400",
        "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=400",
        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400",
        "https://images.unsplash.com/photo-1537047902294-62a40c20a6ae?w=400",
        "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=400",
        "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?w=400",
        "https://images.unsplash.com/photo-1424847651672-bf20a4b0982b?w=400",
        "https://images.unsplash.com/photo-1533777857889-4be7c70b33f7?w=400",
        "https://images.unsplash.com/photo-1544025162-d76694265947?w=400",
        "https://images.unsplash.com/photo-1590947132387-155cc02f3212?w=400",
        "https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=400",
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400"
    )

    private val menuImages = listOf(
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=200",
        "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=200",
        "https://images.unsplash.com/photo-1553621042-f6e147245754?w=200",
        "https://images.unsplash.com/photo-1432139555190-58524dae6a55?w=200",
        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=200",
        "https://images.unsplash.com/photo-1484723091739-30a097e8f929?w=200"
    )

    private val stableRestaurants by lazy {
        val list = mutableListOf<Restaurant>()
        val names = listOf(
            "Burger Lab", "Pizza Palace", "Desi Dhaba", "Sushi Sakura", "Pasta Primo",
            "Chicken Cottage", "Coffee Corner", "Dessert Delight", "Salad Station", "Noodle Night",
            "Taco Time", "Steak House", "Vegan Vibes", "Juice Junction", "Baker's Basket",
            "Kebab King", "Fish Fry", "Grill Guy", "Soup Soul", "Rice Road"
        )
        val categoryOptions = listOf("Burgers", "Pizza", "Sushi", "Desi", "Dessert", "Fast Food")

        for (i in names.indices) {
            val mainCategory = categoryOptions[i % categoryOptions.size]
            list.add(
                Restaurant(
                    id = (i + 1).toString(),
                    name = names[i],
                    imageUrl = foodImages[i % foodImages.size],
                    rating = (4.0f + (i % 10) * 0.1f),
                    reviewCount = 100 + i * 50,
                    deliveryTime = 20 + (i % 5) * 5,
                    deliveryFee = if (i % 3 == 0) 0.0 else 5.0,
                    minOrder = 20.0,
                    distance = "${(0.5 + i * 0.2).let { "%.1f".format(it) }}km",
                    categories = listOf("Food", mainCategory),
                    tags = if (i % 2 == 0) listOf("Brand", "Deal") else listOf("Voucher"),
                    isPromoted = i < 6,
                    discount = if (i % 4 == 0) "50% off" else "Save \$10",
                    monthlyOrders = 500 + i * 100,
                    address = "Street ${i + 1}, Food City",
                    notice = "Fresh ingredients daily. Free delivery on orders above \$30."
                )
            )
        }
        list
    }

    fun getBanners(): List<Banner> = listOf(
        Banner("1", "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800", "50% OFF on First Order"),
        Banner("2", "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800", "Free Delivery This Weekend"),
        Banner("3", "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800", "Flash Sale: Lunch Specials"),
        Banner("4", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800", "Fresh Healthy Options")
    )

    fun getCategories(): List<FoodCategory> = listOf(
        FoodCategory("1", "All", "", "#FF4D00"),
        FoodCategory("2", "Burgers", "", "#FFD600"),
        FoodCategory("3", "Pizza", "", "#FF3D00"),
        FoodCategory("4", "Sushi", "", "#E91E63"),
        FoodCategory("5", "Desi", "", "#4CAF50"),
        FoodCategory("6", "Dessert", "", "#9C27B0"),
        FoodCategory("7", "Fast Food", "", "#FF9800"),
        FoodCategory("8", "Coffee", "", "#795548")
    )

    fun getRestaurants(): List<Restaurant> = stableRestaurants

    fun getMenuItems(restaurantId: String): List<MenuItem> {
        val dishes = when (restaurantId) {
            "1" -> listOf(
                Triple("Zinger Burger", "Crispy fried chicken with lettuce and mayo", menuImages[0]),
                Triple("Beef Tower", "Double beef patty with cheese and pickles", menuImages[1]),
                Triple("Cheese Melt", "Grilled chicken with melted cheddar cheese", menuImages[2])
            )
            "2" -> listOf(
                Triple("Pepperoni Passion", "Classic pepperoni with mozzarella cheese", menuImages[1]),
                Triple("Veggie Feast", "Fresh vegetables on tomato base", menuImages[2]),
                Triple("BBQ Chicken Pizza", "Smoky BBQ sauce with grilled chicken", menuImages[3])
            )
            "3" -> listOf(
                Triple("Chicken Karahi", "Spicy karahi cooked with fresh tomatoes", menuImages[3]),
                Triple("Mutton Handi", "Slow cooked mutton in aromatic spices", menuImages[4]),
                Triple("Seekh Kebab", "Minced beef kebab with green chutney", menuImages[5])
            )
            "4" -> listOf(
                Triple("California Roll", "Crab, avocado and cucumber roll", menuImages[2]),
                Triple("Salmon Nigiri", "Fresh salmon on seasoned rice", menuImages[0]),
                Triple("Tuna Sashimi", "Premium fresh tuna slices", menuImages[1])
            )
            "5" -> listOf(
                Triple("Pasta Alfredo", "Creamy white sauce fettuccine", menuImages[4]),
                Triple("Lasagna", "Layers of pasta, beef and béchamel", menuImages[5]),
                Triple("Spaghetti Meatballs", "Classic tomato sauce with beef meatballs", menuImages[0])
            )
            else -> listOf(
                Triple("Chef's Special", "Today's special dish", menuImages[0]),
                Triple("House Platter", "Mixed selection of our best items", menuImages[1]),
                Triple("Daily Deal", "Value meal with drink included", menuImages[2])
            )
        }

        return dishes.mapIndexed { index, (name, desc, img) ->
            MenuItem(
                id = "item_${restaurantId}_$index",
                restaurantId = restaurantId,
                name = name,
                description = desc,
                imageUrl = img,
                price = 12.0 + index * 4,
                originalPrice = if (index == 0) 16.0 else 0.0,
                rating = 4.5f + (index % 3) * 0.1f,
                monthlyOrders = 200 + index * 50,
                isRecommended = index == 0
            )
        }
    }

    fun getUserProfile(): UserProfile = UserProfile(
        id = "user_1",
        name = "Danny",
        phone = "03260764834",
        avatarUrl = "https://i.pravatar.cc/150?u=danny"
    )

    fun getOrders(): List<Order> = emptyList()

    fun getHotKeywords(): List<String> = listOf("Burger", "Pizza", "Sushi", "Desi", "Fast Food", "Coffee", "Dessert")
}