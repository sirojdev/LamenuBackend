package mimsoft.io.sms

object SmsMapper {
    fun toDto(smsTable: SmsTable?): SmsDto? {
        return if (smsTable == null) null
        else SmsDto(
            id = smsTable.id,
            clientId = smsTable.clientId,
            messageId = smsTable.messageId,
            time = smsTable.time,
            status = smsTable.status
        )
    }

    fun toTable(smsDto: SmsDto?): SmsTable? {
        return if (smsDto == null) null
        else SmsTable(
            id = smsDto.id,
            clientId = smsDto.clientId,
            messageId = smsDto.messageId,
            time = smsDto.time,
            status = smsDto.status
        )
    }
}
