package mimsoft.io.integrate.integrate
const val MERCHANT_INTEGRATE_TABLE="merchant_integrate"
data class MerchantIntegrateDto(
    val merchantId: Long? = null,
    val yandexDeliveryKey: String? = null
)