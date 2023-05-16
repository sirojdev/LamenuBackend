package mimsoft.io.entities.product.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.product.ProductTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp

object ProductRepositoryImpl : ProductRepository {
    override suspend fun getAll(): List<ProductTable?> =
        withContext(Dispatchers.IO) {
            val selectAll =
                """SELECT * FROM product WHERE NOT deleted"""
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val products = mutableListOf<ProductTable?>()
            while (resultSet.next()) {
                products.add(
                    ProductTable(
                        id = resultSet.getLong("id"),
                        menuId = resultSet.getLong("menu_id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEn = resultSet.getString("name_eng"),
                        descriptionUz = resultSet.getString("description_uz"),
                        descriptionRu = resultSet.getString("description_ru"),
                        descriptionEn = resultSet.getString("description_eng"),
                        image = resultSet.getString("image"),
                        costPrice = resultSet.getDouble("price"),
                        deleted = resultSet.getBoolean("deleted"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext products
        }

    override suspend fun get(id: Long?): ProductTable? =
        withContext(Dispatchers.IO) {
            val selectById =
                """SELECT * FROM product WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext ProductTable(
                    id = resultSet.getLong("id"),
                    menuId = resultSet.getLong("menu_id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEn = resultSet.getString("name_eng"),
                    descriptionUz = resultSet.getString("description_uz"),
                    descriptionRu = resultSet.getString("description_ru"),
                    descriptionEn = resultSet.getString("description_eng"),
                    image = resultSet.getString("image"),
                    costPrice = resultSet.getDouble("price"),
                    deleted = resultSet.getBoolean("deleted"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    override suspend fun add(productTable: ProductTable?): Long? =
        withContext(Dispatchers.IO) {
            val insert = """INSERT INTO product (name_uz, name_ru, name_eng, description_uz, description_ru, description_eng, image, price, created, menu_id)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
            val statement = DBManager.connection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, productTable?.nameUz)
            statement.setString(2, productTable?.nameRu)
            statement.setString(3, productTable?.nameEn)
            statement.setString(4, productTable?.descriptionUz)
            statement.setString(5, productTable?.descriptionRu)
            statement.setString(6, productTable?.descriptionEn)
            statement.setString(7, productTable?.image)
            productTable?.costPrice?.let { statement.setDouble(8, it) }
            statement.setTimestamp(9, Timestamp(System.currentTimeMillis()))
            productTable?.menuId?.let { statement.setLong(10, it) }
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            } else {
                return@withContext null
            }
        }

    override suspend fun update(productTable: ProductTable?): Boolean =
        withContext(Dispatchers.IO) {
            val update =
                """UPDATE product
SET name_uz = ?, name_ru = ?, name_eng = ?, description_uz = ?, description_ru = ?, description_eng = ?, image = ?, price = ?, updated = ?, menu_id = ?
WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, productTable?.nameUz)
            statement.setString(2, productTable?.nameRu)
            statement.setString(3, productTable?.nameEn)
            statement.setString(4, productTable?.descriptionUz)
            statement.setString(5, productTable?.descriptionRu)
            statement.setString(6, productTable?.descriptionEn)
            statement.setString(7, productTable?.image)
            statement.setDouble(8, productTable?.costPrice ?: 0.0)
            statement.setTimestamp(9, Timestamp(System.currentTimeMillis()))
            productTable?.menuId?.let { statement.setLong(10, it) }
            productTable?.id?.let { statement.setLong(11, it) }
            statement.executeUpdate()
            return@withContext true
        }

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val delete =
                """UPDATE product SET deleted = true WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            statement.executeUpdate()
            return@withContext true
        }
}