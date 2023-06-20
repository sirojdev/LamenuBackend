package mimsoft.io.features.product.repository

import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductTable
import mimsoft.io.lamenu_bot.dtos.BotUsersDto

interface ProductRepository {
    suspend fun getAll(merchantId: Long?): List<ProductTable?>
    suspend fun get(id: Long?, merchantId: Long?): ProductTable?
    suspend fun add(productTable: ProductTable?): Long?
    suspend fun update(dto: ProductDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
}