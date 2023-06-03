package mimsoft.io.features.category.repository

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryTable

interface CategoryRepository {
    suspend fun getAll(): List<CategoryDto?>
    suspend fun get(id: Long?): CategoryDto?
    suspend fun add(categoryDto: CategoryDto?): Long?
    suspend fun update(categoryDto: CategoryDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}