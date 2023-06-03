package mimsoft.io.features.order

import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.order.utils.OrderTypeEnums

object OrderMapper {

    fun toDto(orderTable: OrderTable?): OrderDto? {
        return if (orderTable != null)
            OrderDto(
                id = orderTable.id,
                status = orderTable.status,
                type = OrderType(
                    name = OrderTypeEnums.valueOf(orderTable.type ?: "")
                ),
                deliveryAt = orderTable.deliveryAt,
                comment = orderTable.comment,
                createdAt = orderTable.createdAt,
                updatedAt = orderTable.updatedAt
            )
        else null
    }

    fun toTable(orderDto: OrderDto?): OrderTable? {
        return if (orderDto != null)
            OrderTable(
                id = orderDto.id,
                status = orderDto.status,
                type = orderDto.type?.name?.name,
                deliveryAt = orderDto.deliveryAt,
                comment = orderDto.comment,
                createdAt = orderDto.createdAt,
                updatedAt = orderDto.updatedAt,
                deliveredAt = orderDto.deliveredAt
            )
        else null
    }
}