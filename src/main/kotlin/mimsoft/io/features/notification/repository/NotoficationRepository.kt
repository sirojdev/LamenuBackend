package mimsoft.io.features.notification.repository

import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.features.notification.NotificationTable
import mimsoft.io.repository.DataPage

interface NotificationRepository {
    suspend fun add(dto: NotificationDto?): Long?
    suspend fun update(dto: NotificationDto?): Boolean
    suspend fun getById(id: Long, merchantId: Long?): NotificationDto?
    suspend fun getAll(merchantId: Long?, limit: Long? = null, offset: Long? = null): DataPage<NotificationDto>?
    suspend fun delete(id: Long, merchantId: Long?): Boolean
}