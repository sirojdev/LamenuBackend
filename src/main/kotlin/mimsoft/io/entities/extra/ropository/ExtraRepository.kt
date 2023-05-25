package mimsoft.io.entities.extra.ropository

import mimsoft.io.entities.extra.ExtraTable

interface ExtraRepository {
    suspend fun getAll(): List<ExtraTable?>
    suspend fun get(id: Long?): ExtraTable?
    suspend fun add(extraTable: ExtraTable?): Long?
    suspend fun update(extraTable: ExtraTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}
