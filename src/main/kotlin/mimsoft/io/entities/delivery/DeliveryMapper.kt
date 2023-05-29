package mimsoft.io.entities.delivery
object DeliveryMapper {
    fun toDeliveryTable(deliveryDto: DeliveryDto?): DeliveryTable? {
        return if (deliveryDto == null) null
        else DeliveryTable(
            id = deliveryDto.id,
            merchantId = deliveryDto.merchantId,
            yandexClientId = deliveryDto.yandexClientId,
            yandexToken = deliveryDto.yandexToken,
            expressId = deliveryDto.expressId,
            expressToken = deliveryDto.expressToken,
        )
    }

    fun toDeliveryDto(deliveryTable: DeliveryTable?): DeliveryDto? {
        return if (deliveryTable == null) null
        else DeliveryDto(
            id = deliveryTable.id,
            merchantId = deliveryTable.merchantId,
            yandexClientId =  deliveryTable.yandexClientId,
            yandexToken = deliveryTable.yandexToken,
            expressId = deliveryTable.expressId,
            expressToken = deliveryTable.expressToken,
        )
    }
}