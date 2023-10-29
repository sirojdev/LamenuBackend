package mimsoft.io.features.payment.payment_integration

data class IntegrationDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val isPaymeEnabled: Boolean? = false,
  val isClickEnabled: Boolean? = false,
  val isApelsinEnabled: Boolean? = false,
  val isPaynetEnabled: Boolean? = false,
  val isCashEnabled: Boolean? = true,
  val isTerminalEnabled: Boolean? = false
)
