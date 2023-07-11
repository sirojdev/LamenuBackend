package mimsoft.io.features.notification.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.notification.NOTIFICATION_TABLE_NAME
import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.features.notification.NotificationMapper
import mimsoft.io.features.notification.NotificationTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object NotificationRepositoryImpl: NotificationRepository {
    val repository: BaseRepository = DBManager
    val mapper = NotificationMapper
    override suspend fun add(dto: NotificationDto?): Long?  =
        DBManager.postData(dataClass = NotificationTable::class, dataObject = mapper.toTable(dto), tableName = NOTIFICATION_TABLE_NAME)

    override suspend fun update(dto: NotificationDto?): Boolean {
        return DBManager.updateData(NotificationTable::class, mapper.toTable(dto), NOTIFICATION_TABLE_NAME)
    }

    override suspend fun getById(id: Long, merchantId: Long?): NotificationDto? {
        val data = repository.getPageData(
            dataClass = NotificationTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = NOTIFICATION_TABLE_NAME
        )?.data?.firstOrNull()
        return mapper.toDto(data)
    }

    override suspend fun getAll(merchantId: Long?): List<NotificationDto?> {
        val data = repository.getPageData(
            dataClass = NotificationTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = NOTIFICATION_TABLE_NAME
        )?.data?.map { mapper.toDto(it) }
        return data ?: emptyList()
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $NOTIFICATION_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}