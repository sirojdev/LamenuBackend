package mimsoft.io.features.operator.socket

import java.sql.Timestamp


data class SenderOrdersToCourierDto(
    val courierId: Long,
    val orderId: Long,
    val offset :Int?,
    val time: Timestamp
)