@file:Suppress("NAME_SHADOWING")

package mimsoft.io.features.notification.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.notification.*
import mimsoft.io.repository.BaseEnums
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.TextModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object NotificationRepositoryImpl : NotificationRepository {

    private val repository: BaseRepository = DBManager
    private val mapper = NotificationMapper
    private val log: Logger = LoggerFactory.getLogger(NotificationRepositoryImpl::class.java)

    override suspend fun getAll(
        merchantId: Long?,
        search: String?,
        filters: String?,
        limit: Int?,
        offset: Int?,
    ): DataPage<NotificationDto> {
        val filters = filters?.uppercase()
        val search = search?.lowercase()
        val query = StringBuilder()
        query.append(
            """
                select id,
                       merchant_id,
                       title_uz,
                       title_ru,
                       title_eng,
                       body_uz,
                       body_ru,
                       body_eng,
                       image,
                       date,
                       is_send_android,
                       is_send_ios,
                       is_send_bot
                from notification
                where not deleted
                  and merchant_id = $merchantId
        """.trimIndent()
        )
        if (filters == null) query.append(" order by created desc")
        if (filters != null && BaseEnums.TIME.name == filters) query.append(" order by date desc")
        if (limit != null) query.append(" limit $limit")
        if (offset != null) query.append(" offset $offset")
        log.info("query: $query")
        val mutableList = mutableListOf<NotificationDto>()
        repository.selectList(query.toString()).forEach {
            mutableList.add(
                NotificationDto(
                    id = it["id"] as Long,
                    merchantId = it["merchant_id"] as? Long,
                    title = TextModel(
                        uz = it["title_uz"] as? String,
                        ru = it["title_ru"] as? String,
                        eng = it["title_eng"] as? String
                    ),
                    body = TextModel(
                        uz = it["body_uz"] as? String,
                        ru = it["body_ru"] as? String,
                        eng = it["body_eng"] as? String
                    ),
                    image = it["image"] as? String,
                    date = it["date"] as? Timestamp,
                    clientId = it["client_id"] as? Long,
                    isSendAndroid = it["is_send_android"] as? Boolean,
                    isSendIos = it["is_send_ios"] as? Boolean,
                    isSendBot = it["is_send_bot"] as? Boolean
                )
            )
        }
        return DataPage(data = mutableList, total = mutableList.size)
    }

    override suspend fun add(dto: NotificationDto?): Long? =
        DBManager.postData(
            dataClass = NotificationTable::class,
            dataObject = mapper.toTable(dto),
            tableName = NOTIFICATION_TABLE_NAME
        )

    override suspend fun update(dto: NotificationDto?): Boolean {
        var rs: Int
        val query = StringBuilder()
        query.append(
            """
                update notification n
                set body_uz         = coalesce(?, n.body_uz),
                    body_ru         = coalesce(?, n.body_ru),
                    body_eng        = coalesce(?, n.body_eng),
                    title_uz        = coalesce(?, n.title_uz),
                    title_ru        = coalesce(?, n.title_ru),
                    title_eng       = coalesce(?, n.title_eng),
                    image           = coalesce(?, n.image),
                    client_id       = coalesce(${dto?.clientId}, n.client_id),
                    is_send_ios     = coalesce(${dto?.isSendIos}, n.is_send_ios),
                    is_send_android = coalesce(${dto?.isSendAndroid}, n.is_send_android),
                    is_send_bot     = coalesce(${dto?.isSendBot}, n.is_send_bot),
                    updated         = ?
                where not deleted
                  and id = ${dto?.id}
                  and merchant_id = ${dto?.merchantId}
        """.trimIndent()
        )
        log.info("query: $query")
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query.toString()).apply {
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
        return rs == 1
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        var rs: Int
        val query = StringBuilder()
        query.append(
            """
                update notification
                set deleted = true
                where not deleted
                  and id = $id
                  and merchant_id = $merchantId
        """.trimIndent()
        )
        log.info("query: $query")
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query.toString()).executeUpdate()
            }
        }
        return rs == 1
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