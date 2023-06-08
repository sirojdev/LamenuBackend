package mimsoft.io.features.category.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable

interface CategoryRepository {
    suspend fun getAllByMerchant(merchantId: Long?): List<CategoryDto?>
    suspend fun getAll(): List<CategoryDto?>
    suspend fun get(id: Long?, merchantId: Long?): CategoryDto?
    suspend fun add(categoryDto: CategoryDto?): Long?
    suspend fun update(categoryDto: CategoryDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}