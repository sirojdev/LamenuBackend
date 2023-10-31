package mimsoft.io.integrate.integrate
const val MERCHANT_INTEGRATE_TABLE = "merchant_integration"

data class MerchantIntegrateDto(
  val merchantId: Long? = null,
  val yandexDeliveryKey: String? = null,
  val iikoApiLogin: String? = null,
  val iikoOrganizationId: String? = null,
  val yandexMapKey: String? = null,
)
