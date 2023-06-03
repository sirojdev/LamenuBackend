package mimsoft.io.features.sms_gateway

object SmsGatewayMapper {
    fun toSmsGatewaysTable(smsGatewaysDto: SmsGatewayDto?): SmsGatewayTable? {
        return if (smsGatewaysDto == null) null
        else SmsGatewayTable(
            id = smsGatewaysDto.id,
            merchantId = smsGatewaysDto.merchantId,
            eskizId = smsGatewaysDto.eskizId,
            eskizToken = smsGatewaysDto.eskizToken,
            playMobileServiceId = smsGatewaysDto.playMobileServiceId,
            playMobileKey = smsGatewaysDto.playMobileKey,
            selected = smsGatewaysDto.selected
        )
    }

    fun toSmsGatewayDto(smsGatewaysTable: SmsGatewayTable?): SmsGatewayDto? {
        return if (smsGatewaysTable == null) null
        else SmsGatewayDto(
            id = smsGatewaysTable.id,
            merchantId = smsGatewaysTable.merchantId,
            eskizId = smsGatewaysTable.eskizId,
            eskizToken = smsGatewaysTable.eskizToken,
            playMobileServiceId = smsGatewaysTable.playMobileServiceId,
            playMobileKey = smsGatewaysTable.playMobileKey,
            selected = smsGatewaysTable.selected
        )
    }
}