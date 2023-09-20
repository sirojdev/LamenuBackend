package mimsoft.io.features.category.repository

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.ClientCategoryDto
import mimsoft.io.features.telegram_bot.Language

interface CategoryRepository {
    suspend fun getAll(merchantId: Long?): List<CategoryDto?>
    suspend fun get(id: Long?, merchantId: Long?): CategoryDto?
    suspend fun add(categoryDto: CategoryDto?): Long?
    suspend fun update(dto: CategoryDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
    suspend fun getCategoryByName(merchantId: Long?, lang: Language, text: String?): CategoryDto?
    suspend fun getAllByClient(merchantId: Long?): List<ClientCategoryDto?>
    suspend fun getCategoryForClientById(id: Long?, merchantId: Long?): ClientCategoryDto?
}