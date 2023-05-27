package mimsoft.io.entities.restaurant.repository

import mimsoft.io.entities.restaurant.RESTAURANT_TABLE_NAME
import mimsoft.io.entities.restaurant.RestaurantTable
import mimsoft.io.repository.DBManager



object RestaurantRepositoryImpl : RestaurantRepository {
    override suspend fun getAll(): List<RestaurantTable?> =
        DBManager.getData(dataClass = RestaurantTable::class, tableName = RESTAURANT_TABLE_NAME)
            .filterIsInstance<RestaurantTable?>()

    override suspend fun get(id: Long?): RestaurantTable? =
        DBManager.getData(dataClass = RestaurantTable::class, id = id, tableName = RESTAURANT_TABLE_NAME)
            .firstOrNull() as RestaurantTable?

    override suspend fun add(restaurantTable: RestaurantTable?): Long? =
        DBManager.postData(dataClass = RestaurantTable::class, dataObject = restaurantTable, tableName = RESTAURANT_TABLE_NAME)


    override suspend fun update(restaurantTable: RestaurantTable?): Boolean =
        DBManager.updateData(dataClass = RestaurantTable::class, dataObject = restaurantTable, tableName = RESTAURANT_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = RESTAURANT_TABLE_NAME, whereValue = id)
}
