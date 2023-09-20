package mimsoft.io.entities.restaurant.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.restaurant.RestaurantTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp


object RestaurantRepositoryImpl : RestaurantRepository {
    override suspend fun getAll(): List<RestaurantTable?> = withContext(Dispatchers.IO) {
        val selectAll = """SELECT * FROM repository WHERE NOT deleted"""
        val statement = DBManager.connection().prepareStatement(selectAll)
        val resultSet = statement.executeQuery()
        val restaurants = mutableListOf<RestaurantTable?>()
        while (resultSet.next()) {
            restaurants.add(
                RestaurantTable(
                    id = resultSet.getLong("id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    logo = resultSet.getString("logo"),
                    domain = resultSet.getString("domain"),
                    deleted = resultSet.getBoolean("deleted"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            )
        }
        return@withContext restaurants
    }

    override suspend fun get(id: Long?): RestaurantTable? = withContext(Dispatchers.IO) {
        val selectById = """SELECT * FROM repository WHERE NOT deleted AND id = ?"""
        val statement = DBManager.connection().prepareStatement(selectById)
        id?.let { statement.setLong(1, it) }
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            return@withContext RestaurantTable(
                id = resultSet.getLong("id"),
                nameUz = resultSet.getString("name_uz"),
                nameRu = resultSet.getString("name_ru"),
                nameEng = resultSet.getString("name_eng"),
                logo = resultSet.getString("logo"),
                domain = resultSet.getString("domain"),
                deleted = resultSet.getBoolean("deleted"),
                created = resultSet.getTimestamp("created"),
                updated = resultSet.getTimestamp("updated")
            )
        } else {
            return@withContext null
        }
    }

    override suspend fun add(restaurantTable: RestaurantTable?): Long? = withContext(Dispatchers.IO) {
        val insert =
            """INSERT INTO repository (name_uz, name_ru, name_eng, logo, domain, created) VALUES (?, ?, ?, ?, ?, ?)"""
        val statement = DBManager.connection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, restaurantTable?.nameUz)
        statement.setString(2, restaurantTable?.nameRu)
        statement.setString(3, restaurantTable?.nameEng)
        statement.setString(4, restaurantTable?.logo)
        statement.setString(5, restaurantTable?.domain)
        statement.setTimestamp(6, Timestamp(System.currentTimeMillis()))
        statement.execute()
        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getLong(1)
        } else {
            return@withContext null
        }
    }

    override suspend fun update(restaurantTable: RestaurantTable?): Boolean = withContext(Dispatchers.IO) {
        val update =
            """UPDATE repository SET name_uz = ?, name_ru = ?, name_eng = ?, logo = ?, domain = ?, updated = ? WHERE NOT deleted AND id = ?"""
        val statement = DBManager.connection().prepareStatement(update)
        statement.setString(1, restaurantTable?.nameUz)
        statement.setString(2, restaurantTable?.nameRu)
        statement.setString(3, restaurantTable?.nameEng)
        statement.setString(4, restaurantTable?.logo)
        statement.setString(5, restaurantTable?.domain)
        statement.setTimestamp(6, Timestamp(System.currentTimeMillis()))
        restaurantTable?.id?.let { statement.setLong(7, it) }
        statement.executeUpdate()
        return@withContext true
    }

    override suspend fun delete(id: Long?): Boolean = withContext(Dispatchers.IO) {
        val delete = """UPDATE repository SET deleted = true WHERE NOT deleted AND id = ?"""
        val statement = DBManager.connection().prepareStatement(delete)
        id?.let { statement.setLong(1, it) }
        statement.executeUpdate()
        return@withContext true
    }
}
