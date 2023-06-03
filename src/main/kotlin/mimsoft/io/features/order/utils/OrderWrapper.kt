package mimsoft.io.features.order.utils

import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.client.UserDto
import mimsoft.io.features.log.OrderLog
import mimsoft.io.features.order.OrderDto

data class OrderWrapper(
    val order: OrderDto? = null,
    val address: mimsoft.io.features.address.AddressDto? = null,
    val products: List<CartItem?>? = null,
    val user: UserDto? = null,
    val logs: List<OrderLog?>? = null
)
