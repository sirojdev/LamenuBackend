package mimsoft.io.features.order

import com.google.gson.Gson
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.order.price.OrderPriceTable
import mimsoft.io.features.order.utils.CartItem
import mimsoft.io.features.order.utils.OrderDetails
import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.payment_type.PaymentTypeDto

object OrderMapper {

    fun toDto(orderTable: OrderTable?): OrderDto? {
        return if (orderTable != null)
            OrderDto(
                id = orderTable.id,
                status = orderTable.status,
                type = orderTable.type?: OrderType.DELIVERY.name,
                paymentTypeDto = PaymentTypeDto(id = orderTable.paymentType, isPaid = orderTable.isPaid)
            )
        else null
    }

    fun toDetails(orderPriceTable: OrderPriceTable? = null, orderTable: OrderTable? = null): OrderDetails? {
        return if (orderTable != null)
            OrderDetails(
                createdAt = orderTable.createdAt,
                deliveryAt = orderTable.deliveryAt,
                deliveredAt = orderTable.deliveredAt,
                updatedAt = orderTable.updatedAt,
                totalPrice = orderPriceTable?.totalPrice,
                totalDiscount = orderPriceTable?.totalDiscount,
                comment = orderTable.comment,
                courierId = orderTable.courierId,
                collectorId = orderTable.collectorId

            )
        else null
    }

    fun toTable(
        orderDto: OrderDto? = null,
        user: UserDto? = null,
        address: AddressDto? = null,
        products: List<CartItem?>? = null,
        details: OrderDetails? = null
    ): OrderTable? {
        val productsJson = Gson().toJson(products)
        return if (orderDto != null)
            OrderTable(
                id = orderDto.id,
                userId = user?.id,
                userPhone = user?.phone,
                type = orderDto.type,
                products = productsJson,
                status = orderDto.status,
                addLat = address?.latitude,
                addLong = address?.longitude,
                addDesc = address?.description,
                createdAt = details?.createdAt,
                deliveryAt = details?.deliveryAt,
                deliveredAt = details?.deliveredAt,
                updatedAt = details?.updatedAt,
                comment = details?.comment,
                courierId = details?.courierId,
                collectorId = details?.collectorId,
                productCount = orderDto.productCount,
                grade = orderDto.grade
            )
        else null
    }
}