package mimsoft.io.features.checkout

import com.fasterxml.jackson.annotation.JsonInclude
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.promo.PromoDto
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckoutRequestDto(
    val id: Long? = null,
    val order: OrderWrapper? = null,
    val promo: PromoDto? = null,
    val totalPrice: Double?=null
)