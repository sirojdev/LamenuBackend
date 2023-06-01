package mimsoft.io.sms

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object SmsService {
    val repository: BaseRepository = DBManager
    val mapper: SmsMapper = SmsMapper

    suspend fun getAll(): List<SmsDto?> {
        return repository.getData(SmsTable::class, tableName = "sms")
            .map { mapper.toDto(it as SmsTable) }
    }

    suspend fun getByMessageId(messageId: Long): List<SmsDto?> {
        return repository.getPageData(
            SmsTable::class,
            tableName = "sms",
            where = mapOf("message_id" to messageId as Any
        ))?.data?.map { mapper.toDto(it) } ?: emptyList()
    }

    suspend fun get(id: Long): SmsDto? {
        return repository.getData(SmsTable::class, id, tableName = "sms")
            .map { mapper.toDto(it as SmsTable) }
            .firstOrNull()
    }

    suspend fun post(smsDto: SmsDto?): Long? {
        return repository.postData(SmsTable::class, mapper.toTable(smsDto), tableName = "sms")
    }

    suspend fun update(smsDto: SmsDto?): Boolean {
        return repository.updateData(SmsTable::class, mapper.toTable(smsDto), tableName = "sms")
    }

    suspend fun delete(id: Long): Boolean {
        return repository.deleteData("sms", where = "id", whereValue = id)
    }

    suspend fun deleteByMessageId(messageId: Long): Boolean {
        return repository.deleteData("sms", where = "message_id", whereValue = messageId)
    }
}