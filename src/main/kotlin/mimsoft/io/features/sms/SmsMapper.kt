package mimsoft.io.features.sms

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.message.MessageDto

object SmsMapper {
  fun toDto(smsTable: SmsTable?): SmsDto? {
    return if (smsTable == null) null
    else
      SmsDto(
        id = smsTable.id,
        merchantId = smsTable.merchantId,
        clientCount = smsTable.clientCount,
        message = MessageDto(id = smsTable.messageId),
        time = smsTable.time,
        client = UserDto(id = smsTable.clientId),
        status = Status.valueOf(smsTable.status ?: "NOT_SENT"),
        context = smsTable.context
      )
  }

  fun toTable(smsDto: SmsDto?): SmsTable? {
    return if (smsDto == null) null
    else
      SmsTable(
        id = smsDto.id,
        merchantId = smsDto.merchantId,
        clientCount = smsDto.clientCount,
        messageId = smsDto.message?.id,
        clientId = smsDto.client?.id,
        time = smsDto.time,
        status = smsDto.status?.name
      )
  }
}
