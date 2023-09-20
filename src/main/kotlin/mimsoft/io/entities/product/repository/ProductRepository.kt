package mimsoft.io.entities.product.repository

import mimsoft.io.entities.product.ProductTable

interface ProductRepository {
    suspend fun getAll(): List<ProductTable?>
    suspend fun get(id: Long?): ProductTable?
    suspend fun add(productTable: ProductTable?): Long?
    suspend fun update(productTable: ProductTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}