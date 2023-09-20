package mimsoft.io.features.sms

import mimsoft.io.features.message.MessageDto
import java.sql.Timestamp

object SmsMapper {
    fun toDto(smsTable: SmsTable?): SmsDto? {
        return if (smsTable == null) null
        else SmsDto(
            id = smsTable.id,
            merchantId = smsTable.merchantId,
            clientCount = smsTable.clientCount,
            message = MessageDto(id = smsTable.messageId),
            time = smsTable.time.toString(),
            clientId = smsTable.clientId,
            status = Status.valueOf(smsTable.status ?: "NOT_SENT")
        )
    }

    fun toTable(smsDto: SmsDto?): SmsTable? {
        return if (smsDto == null) null
        else SmsTable(
            id = smsDto.id,
            merchantId = smsDto.merchantId,
            clientCount = smsDto.clientCount,
            messageId = smsDto.message?.id,
            clientId = smsDto.clientId,
            time = smsDto.time?.let { Timestamp.valueOf(it) },
            status = smsDto.status?.name
        )
    }
}
