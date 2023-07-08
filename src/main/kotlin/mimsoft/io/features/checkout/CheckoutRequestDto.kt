package mimsoft.io.features.checkout

import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.promo.PromoDto

data class CheckoutRequestDto(
    val id: Long? = null,
    val order: OrderWrapper? = null,
    val promo: PromoDto? = null
)