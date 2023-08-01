package mimsoft.io.features.order

import mimsoft.io.features.cart.CartItem
import java.sql.Timestamp

data class ClientOrderDto(
    val id: Long? = null,
    val created: Timestamp? = null,
    val totalPrice: Long? = null,
    val product: List<CartItem>? = null,
    val status: String? = null,
    var imageList: List<String>? = null
)
