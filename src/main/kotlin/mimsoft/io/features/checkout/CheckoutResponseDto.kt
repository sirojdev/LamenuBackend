package mimsoft.io.features.checkout

data class CheckoutResponseDto(
    val productCount: Long? = null,
    val discount: Long? = null,
    val promoCode: String? = null,
    val deliveryPrice: Double? = null,
    val total: Double? = null
)