package mimsoft.io.features.notification.repository

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.notification.NOTIFICATION_TABLE_NAME
import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.features.notification.NotificationMapper
import mimsoft.io.features.notification.NotificationTable
import mimsoft.io.features.visit.VISIT_TABLE_NAME
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.features.visit.VisitService
import mimsoft.io.integrate.onlinePbx.Data
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import java.sql.Timestamp

object NotificationRepositoryImpl : NotificationRepository {
    val repository: BaseRepository = DBManager
    val mapper = NotificationMapper
    override suspend fun add(dto: NotificationDto?): Long? =
        DBManager.postData(
            dataClass = NotificationTable::class,
            dataObject = mapper.toTable(dto),
            tableName = NOTIFICATION_TABLE_NAME
        )

    override suspend fun update(dto: NotificationDto?): Boolean {
        val query = "update $NOTIFICATION_TABLE_NAME set " +
                "body_uz = ?, " +
                "body_ru = ?, " +
                "body_eng = ?, " +
                "title_uz = ?, " +
                "title_ru = ?, " +
                "title_eng = ?, " +
                " image = ?," +
                " client_id = ${dto?.clientId}," +
                " is_send_ios = ${dto?.isSendIos}, " +
                " is_send_android = ${dto?.isSendAndroid}, " +
                " is_send_bot = ${dto?.isSendBot}, " +
                "updated = ? where merchant_id = ${dto?.merchantId} and id = ${dto?.id}"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { notification ->
                    notification.setString(1, dto?.body?.uz)
                    notification.setString(2, dto?.body?.ru)
                    notification.setString(3, dto?.body?.eng)
                    notification.setString(4, dto?.title?.uz)
                    notification.setString(5, dto?.title?.ru)
                    notification.setString(6, dto?.title?.eng)
                    notification.setString(7, dto?.image)
                    notification.setTimestamp(8, Timestamp(System.currentTimeMillis()))
                }
            }
        }
        return true
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

    override suspend fun getAll(merchantId: Long?, limit: Long?, offset: Long?): DataPage<NotificationDto> {
        val data = repository.getPageData(
            dataClass = NotificationTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = NOTIFICATION_TABLE_NAME,
            limit = limit?.toInt(),
            offset = offset?.toInt()
        )
        val data2 = data?.data?.map { mapper.toDto(it) }
        return DataPage(data2!!, data.total)
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $NOTIFICATION_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    suspend fun getClient(merchantId: Long?, userId: Long?): List<NotificationDto?> {
        val data = repository.getPageData(
            dataClass = NotificationTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "client_id" to userId as Any
            ),
            tableName = NOTIFICATION_TABLE_NAME
        )?.data?.map { mapper.toDto(it) }
        return data ?: emptyList()
    }
}