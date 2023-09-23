package mimsoft.io.features.notification.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.notification.NOTIFICATION_TABLE_NAME
import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.features.notification.NotificationMapper
import mimsoft.io.features.notification.NotificationTable
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
        var response = 0
        val query = "update $NOTIFICATION_TABLE_NAME n set " +
                " body_uz = COALESCE(?, n.body_uz), " +
                " body_ru = COALESCE(?, n.body_ru), " +
                " body_eng = COALESCE(?, n.body_eng), " +
                " title_uz = COALESCE(?, n.title_uz), " +
                " title_ru = COALESCE(?, n.title_ru), " +
                " title_eng = COALESCE(?, n.title_eng), " +
                " image = COALESCE(?, n.image)," +
                " client_id = COALESCE(${dto?.clientId}, n.client_id)," +
                " is_send_ios = COALESCE(${dto?.isSendIos}, n.is_send_ios), " +
                " is_send_android = COALESCE(${dto?.isSendAndroid}, n.is_send_android), " +
                " is_send_bot = COALESCE(${dto?.isSendBot}, n.is_send_bot), " +
                " updated = ? where merchant_id = ${dto?.merchantId} and id = ${dto?.id}"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                response = it.prepareStatement(query).apply {
                    this.setString(1, dto?.body?.uz)
                    this.setString(2, dto?.body?.ru)
                    this.setString(3, dto?.body?.eng)
                    this.setString(4, dto?.title?.uz)
                    this.setString(5, dto?.title?.ru)
                    this.setString(6, dto?.title?.eng)
                    this.setString(7, dto?.image)
                    this.setTimestamp(8, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return response == 1
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

    override suspend fun getAll(
        merchantId: Long?,
        limit: Int?,
        offset: Int?,
        search: String?
    ): DataPage<NotificationDto> {
        val dataPage = repository.getPageData(
            dataClass = NotificationTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = NOTIFICATION_TABLE_NAME,
            limit = limit,
            offset = offset
        )
        return DataPage(
            data = dataPage?.data?.map { mapper.toDto(it) },
            total = dataPage?.total
        )
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        var rs = 0
        val query = "update $NOTIFICATION_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
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