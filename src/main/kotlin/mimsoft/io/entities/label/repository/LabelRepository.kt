package mimsoft.io.entities.label.repository

import mimsoft.io.entities.label.LabelTable

interface LabelRepository {
    suspend fun getAll(): List<LabelTable?>
    suspend fun get(id: Long?): LabelTable?
    suspend fun add(labelTable: LabelTable?): Long?
    suspend fun update(labelTable: LabelTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}