package mimsoft.io.message

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import mimsoft.io.entities.client.repository.UserRepository
import mimsoft.io.entities.client.repository.UserRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.services.SmsSender
import mimsoft.io.sms.SmsService
import java.sql.Timestamp

object MessageService {

    val repository: BaseRepository = DBManager
    val mapper: MessageMapper = MessageMapper
    private val smsService: SmsService = SmsService
    private val smsSender: SmsSender = SmsSender
    private val userRepository: UserRepository = UserRepositoryImpl
    suspend fun getAll(): List<MessageDto?> {
        val messages = repository.getData(MessageTable::class, tableName = "message")
            .map { mapper.toDto(it as MessageTable) }
        for (message in messages) {
            message?.smss = smsService.getByMessageId(message?.id ?: 0)
        }
        return messages
    }

    suspend fun get(id: Long): MessageDto? {
        return repository.getData(MessageTable::class, id, tableName = "message")
            .map { mapper.toDto(it as MessageTable) }
            .firstOrNull()?.copy(smss = smsService.getByMessageId(id))
    }

    suspend fun post(messageDto: MessageDto?): Long? {
        val messageId =  repository.postData(MessageTable::class,
            mapper.toTable(messageDto?.copy(time = Timestamp(System.currentTimeMillis()).toString())),
            tableName = "message")


        coroutineScope {
            messageDto?.smss?.forEach {
                launch {
                    userRepository.get(it?.clientId ?: 0)?.phone?.let { it1 ->
                        smsSender.send(
                            phone = it1,
                            content = messageDto.content,
                            merchantId = messageDto.merchantId
                        )
                    }
                    smsService.post(it?.copy(messageId = messageId))
                }
            }
        }
        return messageId
    }

    suspend fun update(messageDto: MessageDto?): Boolean {
        return repository.updateData(MessageTable::class, mapper.toTable(messageDto), tableName = "message")
    }

    suspend fun delete(id: Long): Boolean {
        coroutineScope {
            smsService.deleteByMessageId(id)
        }
        return repository.deleteData("message", where = "id", whereValue = id)
    }
}