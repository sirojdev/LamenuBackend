package mimsoft.io.integrate.uzum.module

class UzumRegisterResponse(
  var errorCode: Int? = null,
  var message: String? = null,
  var result: UzumResult? = null
)

data class UzumResult(
  var orderId: String? = null,
  var paymentRedirectUrl: String? = null,
  var operationId: String? = null
)
