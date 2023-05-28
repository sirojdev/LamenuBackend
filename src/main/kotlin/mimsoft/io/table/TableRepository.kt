package mimsoft.io.table

import mimsoft.io.table.TableTable

interface TableRepository {
    suspend fun getAll(): List<TableDto?>
    suspend fun get(id: Long?): TableDto?
    suspend fun add(tableDto: TableDto?): Long?
    suspend fun update(tableDto: TableDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}