package mimsoft.io.features.sms_gateway

object SmsGatewayMapper {
    fun toSmsGatewaysTable(smsGatewaysDto: SmsGatewayDto?): SmsGatewayTable? {
        return if (smsGatewaysDto == null) null
        else SmsGatewayTable(
            id = smsGatewaysDto.id,
            merchantId = smsGatewaysDto.merchantId,
            eskizUsername = smsGatewaysDto.eskizEmail,
            eskizPassword = smsGatewaysDto.eskizPassword,
            playMobileUsername = smsGatewaysDto.playMobileUsername,
            playMobilePassword = smsGatewaysDto.playMobilePassword,
            selected = smsGatewaysDto.selected
        )
    }

    fun toSmsGatewayDto(smsGatewaysTable: SmsGatewayTable?): SmsGatewayDto? {
        return if (smsGatewaysTable == null) null
        else SmsGatewayDto(
            id = smsGatewaysTable.id,
            merchantId = smsGatewaysTable.merchantId,
            eskizEmail = smsGatewaysTable.eskizUsername,
            eskizPassword = smsGatewaysTable.eskizPassword,
            playMobileUsername = smsGatewaysTable.playMobileUsername,
            playMobilePassword = smsGatewaysTable.playMobilePassword,
            selected = smsGatewaysTable.selected
        )
    }
}