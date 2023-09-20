package mimsoft.io.features.extra.ropository

import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.extra.ExtraTable

interface ExtraRepository {
    suspend fun getAll(merchantId: Long?): List<ExtraTable?>
    suspend fun get(id: Long?, merchantId: Long?): ExtraTable?
    suspend fun add(extraTable: ExtraTable?): Long?
    suspend fun update(dto: ExtraDto): Boolean
    suspend fun delete(id: Long, merchantId: Long?): Boolean
}
