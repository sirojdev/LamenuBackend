package mimsoft.io.message

import mimsoft.io.sms.SmsMapper

object MessageMapper {
    fun toDto(messageTable: MessageTable?): MessageDto? {
        return if (messageTable == null) null
        else MessageDto(
            id = messageTable.id,
            content = messageTable.content,
            time = messageTable.time,
            smss = messageTable.smss?.map { SmsMapper.toDto(it) }
        )
    }

    fun toTable(messageDto: MessageDto?): MessageTable? {
        return if (messageDto == null) null
        else MessageTable(
            id = messageDto.id,
            content = messageDto.content,
            time = messageDto.time,
            smss = messageDto.smss?.map { SmsMapper.toTable(it) }
        )
    }
}