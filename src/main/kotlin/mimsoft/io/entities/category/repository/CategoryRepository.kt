package mimsoft.io.entities.category.repository

import mimsoft.io.entities.category.CategoryTable

interface CategoryRepository {
    suspend fun getAll(): List<CategoryTable?>
    suspend fun get(id: Long?): CategoryTable?
    suspend fun add(categoryTable: CategoryTable?): Long?
    suspend fun update(categoryTable: CategoryTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}