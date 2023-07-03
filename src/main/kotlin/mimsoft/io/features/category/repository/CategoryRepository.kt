package mimsoft.io.features.category.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.lamenu_bot.dtos.BotUsersDto

interface CategoryRepository {
    suspend fun getAll(merchantId: Long?): List<CategoryDto?>
    suspend fun get(id: Long?, merchantId: Long?): CategoryDto?
    suspend fun add(categoryDto: CategoryDto?): Long?
    suspend fun update(dto: CategoryDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
    fun getCategoryByName(profile: BotUsersDto, text: String): CategoryDto?
}