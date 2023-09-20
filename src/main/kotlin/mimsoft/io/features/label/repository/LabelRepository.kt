package mimsoft.io.features.label.repository

import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.label.LabelTable

interface LabelRepository {
    suspend fun getAll(merchantId: Long?): List<LabelTable?>
    suspend fun get(id: Long?, merchantId: Long?): LabelTable?
    suspend fun add(extraTable: LabelTable?): Long?
    suspend fun update(dto: LabelDto): Boolean
    suspend fun delete(id: Long, merchantId: Long?): Boolean
}