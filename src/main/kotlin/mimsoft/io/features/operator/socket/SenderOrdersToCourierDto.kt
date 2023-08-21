package mimsoft.io.features.operator.socket

import java.sql.Timestamp


data class SendOrdersToCourierDto(
    val courierId: Long,
    val orderId: Long,
    val offset :Int?,
    val time: Timestamp
)