package mimsoft.io.features.sms

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.message.MessageDto
import mimsoft.io.features.promo.PROMO_TABLE_NAME
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import java.sql.Timestamp

object SmsService {
    val repository: BaseRepository = DBManager
    val mapper: SmsMapper = SmsMapper

    suspend fun getAll(
        merchantId: Long?,
        limit: Int? = null,
        offset: Int? = null
    ): DataPage<SmsDto> {
        val defaultLimit = 10
        val defaultOffset = 0
        val query = StringBuilder()
        query.append(
            """
            select s.id         s_id,
            s.time              s_time,
            status,
            m.id                m_id,
            content,
            (select count(*) from users u where u.merchant_id = $merchantId) as count
            from sms s
            left join message m on s.message_id = m.id
            where s.merchant_id = $merchantId
            and not s.deleted
            and not m.deleted
            order by s.created desc
        """.trimIndent()
        )
        if (limit != null) query.append(" limit $limit")
        if (offset != null) query.append(" offset $offset")
        else query.append(" limit $defaultLimit offset $defaultOffset")
        var totalCount = 0
        val tName = SMS_TABLE
        return withContext(DBManager.databaseDispatcher) {
            val resultList = mutableListOf<SmsDto>()
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                    while (rs.next()) {
                        val dto = SmsDto(
                            id = rs.getLong("s_id"),
                            time = rs.getString("s_time"),
                            status = Status.valueOf(rs.getString("status")),
                            clientCount = rs.getLong("count"),
                            message = MessageDto(
                                id = rs.getLong("m_id"),
                                content = rs.getString("content"),
                            )
                        )
                        resultList.add(dto)
                    }
                totalCount = tName.let { DBManager.getDataCount(it)!! }
            }
            return@withContext DataPage(resultList, totalCount)
        }
    }


    suspend fun getAll2(
        merchantId: Long?,
        limit: Int? = null,
        offset: Int? = null
    ): List<SmsDto?> {
        val query =
            """
                select s.id   s_id,
                    s.time s_time,
                    status,
                    m.id   m_id,
                    content,
                    (select count(*) from users u where u.merchant_id = $merchantId) as count 
                    from sms s
                left join message m on s.message_id = m.id
                where s.merchant_id = $merchantId 
                    and not s.deleted
                    and not m.deleted limit $limit offset $offset 
            """
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<SmsDto>()
                while (rs.next()) {
                    val dto = SmsDto(
                        id = rs.getLong("s_id"),
                        time = rs.getString("s_time"),
                        status = Status.valueOf(rs.getString("status")),
                        message = MessageDto(
                            id = rs.getLong("m_id"),
                            content = rs.getString("content"),
                        ),
                        clientCount = rs.getLong("count")
                    )
                    list.add(dto)
                }
                return@withContext list
            }
        }
    }

    suspend fun getByMessageId(messageId: Long): List<SmsDto?> {
        return repository.getPageData(
            SmsTable::class,
            tableName = SMS_TABLE,
            where = mapOf(
                "message_id" to messageId as Any
            )
        )?.data?.map { SmsMapper.toDto(it) } ?: emptyList()
    }

    suspend fun get(id: Long, merchantId: Long?): SmsDto? {
        return repository.getPageData(
            SmsTable::class,
            where = mapOf(
                ("id" to id as Any),
                ("merchant_id" to merchantId as Any)
            ),
            tableName = SMS_TABLE
        )?.data?.map { SmsMapper.toDto(it) }?.firstOrNull()
    }

    suspend fun post(smsDto: SmsDto?): Long? {
        return repository.postData(
            SmsTable::class,
            SmsMapper.toTable(smsDto)?.copy(time = Timestamp(System.currentTimeMillis())),
            tableName = SMS_TABLE
        )
    }

    suspend fun update(smsDto: SmsDto?): Boolean {
        return repository.updateData(SmsTable::class, SmsMapper.toTable(smsDto), tableName = SMS_TABLE)
    }

    suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $SMS_TABLE set deleted = true where id = $id and merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use { it.prepareStatement(query).execute() }
        }
        return true
    }

    suspend fun deleteByMessageId(messageId: Long): Boolean {
        return repository.deleteData(SMS_TABLE, where = "message_id", whereValue = messageId)
    }
}