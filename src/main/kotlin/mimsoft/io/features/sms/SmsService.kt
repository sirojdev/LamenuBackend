package mimsoft.io.features.sms

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.message.MessageDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import java.sql.Timestamp
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object SmsService {
    val repository: BaseRepository = DBManager
    val mapper: SmsMapper = SmsMapper


    suspend fun getAll(
        merchantId: Long?,
        limit: Int? = null,
        offset: Int? = null
    ): List<SmsDto> {

        val where = " where s.merchant_id = $merchantId \n" +
                "                    and not s.deleted\n" +
                "                    and not m.deleted "
        val columns = "s.id   s_id,\n" +
                "                    s.time s_time,\n" +
                "                    status,\n" +
                "                    m.id   m_id,\n" +
                "                    content,\n" +
                "                    (select count(*) from users u where u.merchant_id = $merchantId) as count"
        val tableName = " sms s left join message m on s.message_id = m.id"

        val query = "select $columns from $tableName where $where limit $limit offset $offset "
        println("query = $query")

        val resultList = arrayListOf<SmsDto>()
        withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                val constructor = SmsTable::class.primaryConstructor
                    ?: throw IllegalStateException("Data class must have a primary constructor")

                while (resultSet.next()) {
                    val parameters = constructor.parameters.associateWith { parameter ->
                        val columnName = parameter.name?.let { DBManager.camelToSnakeCase(it) }
                        resultSet.getObject(columnName)
                    }
                    val instance = constructor.callBy(parameters)
                    resultList.add(mapper.toDto(instance)!!)
                }
            }
        }

        val totalCount = tableName.let { DBManager.getDataCount(it) }

        return totalCount?.let { DataPage(resultList, it) } as List<SmsDto>
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
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
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
            repository.connection().use { it.prepareStatement(query).apply { this.closeOnCompletion() }.execute() }
        }
        return true
    }

    suspend fun deleteByMessageId(messageId: Long): Boolean {
        return repository.deleteData(SMS_TABLE, where = "message_id", whereValue = messageId)
    }
}