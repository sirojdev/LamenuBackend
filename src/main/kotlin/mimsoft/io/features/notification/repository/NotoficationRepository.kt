package mimsoft.io.features.notification.repository

import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.repository.DataPage

interface NotificationRepository {

    suspend fun getAll(
        merchantId: Long?,
        search: String? = null,
        filters: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): DataPage<NotificationDto>?

    suspend fun add(dto: NotificationDto?): Long?
    suspend fun update(dto: NotificationDto?): Boolean
    suspend fun delete(id: Long, merchantId: Long?): Boolean
    suspend fun getById(id: Long, merchantId: Long?): NotificationDto?
}