package mimsoft.io.features.notification.repository

import mimsoft.io.features.notification.NotificationDto

interface NotificationRepository {
    suspend fun add(dto: NotificationDto?): Long?
    suspend fun update(dto: NotificationDto?): Boolean
    suspend fun getById(id: Long, merchantId: Long?): NotificationDto?
    suspend fun getAll(merchantId: Long?): List<NotificationDto?>
    suspend fun delete(id: Long, merchantId: Long?): Boolean
}