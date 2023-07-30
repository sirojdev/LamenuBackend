package mimsoft.io.features.order.utils

import com.fasterxml.jackson.annotation.JsonInclude
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.log.OrderLog
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.price.OrderPriceDto
import org.jetbrains.annotations.NonNls

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrderWrapper(
    val details: OrderDetails? = null,
    val order: OrderDto? = null,
    val address: AddressDto? = null,
    val products: List<CartItem?>? = null,
    val user: UserDto? = null,
    val price: OrderPriceDto? = null,
    val logs: List<OrderLog?>? = null,
    val orderPrice: OrderPriceDto?=null
)
