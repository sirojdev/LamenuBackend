package mimsoft.io.features.table

import mimsoft.io.features.table.TableTable

interface TableRepository {
    suspend fun getAll(): List<TableTable?>
    suspend fun get(id: Long?): TableTable?
    suspend fun add(tableTable: TableTable?): Long?
    suspend fun update(tableTable: TableTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}