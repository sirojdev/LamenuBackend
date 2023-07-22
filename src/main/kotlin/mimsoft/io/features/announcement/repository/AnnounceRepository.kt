package mimsoft.io.features.announcement.repository

import mimsoft.io.features.announcement.AnnouncementDto

interface AnnounceRepository {
    suspend fun add(dto: AnnouncementDto?): Long?
    suspend fun update(dto: AnnouncementDto?): Boolean
    suspend fun getById(id: Long, merchantId: Long?): AnnouncementDto?
    suspend fun getAll(merchantId: Long?): List<AnnouncementDto?>
    suspend fun delete(id: Long, merchantId: Long?): Boolean
}