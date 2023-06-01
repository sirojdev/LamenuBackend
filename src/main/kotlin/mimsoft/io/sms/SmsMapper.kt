package mimsoft.io.sms

import java.sql.Timestamp

object SmsMapper {
    fun toDto(smsTable: SmsTable?): SmsDto? {
        return if (smsTable == null) null
        else SmsDto(
            id = smsTable.id,
            clientId = smsTable.clientId,
            messageId = smsTable.messageId,
            time = smsTable.time.toString(),
            status = Status.valueOf(smsTable.status ?: "NOT_SENT")
        )
    }

    fun toTable(smsDto: SmsDto?): SmsTable? {
        return if (smsDto == null) null
        else SmsTable(
            id = smsDto.id,
            clientId = smsDto.clientId,
            messageId = smsDto.messageId,
            time = smsDto.time?.let { Timestamp.valueOf(it) },
            status = smsDto.status?.name
        )
    }
}
