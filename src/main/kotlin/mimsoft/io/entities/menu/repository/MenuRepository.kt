package mimsoft.io.entities.menu.repository

import mimsoft.io.entities.menu.MenuTable

interface MenuRepository {
    suspend fun getAll(): List<MenuTable?>
    suspend fun get(id: Long?): MenuTable?
    suspend fun add(menuTable: MenuTable?): Long?
    suspend fun update(menuTable: MenuTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}