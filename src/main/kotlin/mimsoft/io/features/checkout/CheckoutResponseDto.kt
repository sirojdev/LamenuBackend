package mimsoft.io.features.checkout

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckoutResponseDto(
    val productCount: Long? = null,
    val discountProduct: Long? = null,
    val discountDelivery: Long? = null,
    val promoCode: String? = null,
    val deliveryPrice: Double? = null,
    val total: Double? = null
)