package mimsoft.io.features.message

import mimsoft.io.sms.SmsMapper
import java.sql.Timestamp

object MessageMapper {
    fun toDto(messageTable: MessageTable?): MessageDto? {
        return if (messageTable == null) null
        else MessageDto(
            id = messageTable.id,
            content = messageTable.content,
            time = messageTable.time.toString(),
        )
    }

    fun toTable(messageDto: MessageDto?): MessageTable? {
        return if (messageDto == null) null
        else MessageTable(
            id = messageDto.id,
            content = messageDto.content,
            time = messageDto.time.let { Timestamp.valueOf(it)}
        )
    }
}