package mimsoft.io.entities.order.utils

import mimsoft.io.entities.address.AddressDto
import mimsoft.io.entities.client.UserDto
import mimsoft.io.entities.log.OrderLog
import mimsoft.io.entities.order.OrderDto

data class OrderWrapper(
    val order: OrderDto? = null,
    val address: AddressDto? = null,
    val products: List<CartItem?>? = null,
    val user: UserDto? = null,
    val logs: List<OrderLog?>? = null
)
