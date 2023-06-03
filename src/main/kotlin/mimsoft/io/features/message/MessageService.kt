package mimsoft.io.features.message

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.features.sms.SmsService
import java.sql.Timestamp

object MessageService {

    val repository: BaseRepository = DBManager
    val mapper: MessageMapper = MessageMapper
    private val smsService: SmsService = SmsService
    suspend fun getAll(): List<MessageDto?> {
        val messages = repository.getData(MessageTable::class, tableName = "message")
            .map { MessageMapper.toDto(it as MessageTable) }
        for (message in messages) {
            message?.smss = smsService.getByMessageId(message?.id ?: 0)
        }
        return messages
    }

    suspend fun get(id: Long): MessageDto? {
        return repository.getData(MessageTable::class, id, tableName = "message")
            .map { MessageMapper.toDto(it as MessageTable) }
            .firstOrNull()?.copy(smss = smsService.getByMessageId(id))
    }

    suspend fun post(messageDto: MessageDto?): Long? {
        return repository.postData(
            MessageTable::class,
            MessageMapper.toTable(messageDto?.copy(time = Timestamp(System.currentTimeMillis()).toString())),
            tableName = "message")
    }

    suspend fun update(messageDto: MessageDto?): Boolean {
        return repository.updateData(MessageTable::class, MessageMapper.toTable(messageDto), tableName = "message")
    }

    suspend fun delete(id: Long): Boolean {
        coroutineScope {
            smsService.deleteByMessageId(id)
        }
        return repository.deleteData("message", where = "id", whereValue = id)
    }
}