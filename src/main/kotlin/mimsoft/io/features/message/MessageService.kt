package mimsoft.io.features.message

import java.sql.Timestamp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.sms.SmsService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.services.sms.SmsSenderService

object MessageService {

  val repository: BaseRepository = DBManager
  val mapper: MessageMapper = MessageMapper
  private val smsService: SmsService = SmsService
  private val smsSender: SmsSenderService = SmsSenderService
  private val userRepository: UserRepository = UserRepositoryImpl

  suspend fun getAll(merchantId: Long?): List<MessageDto?> {
    val messages =
      repository.getData(MessageTable::class, tableName = "message", merchantId = merchantId).map {
        MessageMapper.toDto(it as MessageTable)
      }
    /* for (message in messages) {
      message?.smss = smsService.getByMessageId(message?.id ?: 0)
    }*/
    return messages
  }

  suspend fun get(id: Long, merchantId: Long?): MessageDto? {
    return repository
      .getData(MessageTable::class, id, tableName = "message", merchantId = merchantId)
      .map { MessageMapper.toDto(it as MessageTable) }
      .firstOrNull()
      ?.copy(smss = smsService.getByMessageId(id))
  }

  suspend fun post(messageDto: MessageDto?): Long? {

    val messageId =
      repository.postData(
        MessageTable::class,
        mapper.toTable(messageDto?.copy(time = Timestamp(System.currentTimeMillis()).toString())),
        tableName = "message"
      )

    coroutineScope {
      messageDto?.smss?.forEach {
        launch {
          userRepository.get(it?.client?.id ?: 0)?.phone?.let { it1 ->
            smsSender.send(
              phone = it1,
              content = messageDto.content,
              merchantId = messageDto.merchantId
            )
          }
          smsService.post(it?.copy(message = MessageDto(id = messageId)))
        }
      }
    }
    return messageId
  }

  suspend fun update(messageDto: MessageDto?): Boolean {
    return repository.updateData(
      MessageTable::class,
      MessageMapper.toTable(messageDto),
      tableName = "message"
    )
  }

  suspend fun delete(id: Long): Boolean {
    coroutineScope { smsService.deleteByMessageId(id) }
    return repository.deleteData("message", where = "id", whereValue = id)
  }
}
