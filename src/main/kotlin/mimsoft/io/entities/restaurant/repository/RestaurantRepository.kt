package mimsoft.io.entities.restaurant.repository

import mimsoft.io.entities.restaurant.RestaurantTable

interface RestaurantRepository {
    suspend fun getAll(): List<RestaurantTable?>
    suspend fun get(id: Long?): RestaurantTable?
    suspend fun add(restaurantTable: RestaurantTable?): Long?
    suspend fun update(restaurantTable: RestaurantTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}