package mimsoft.io.integrate.uzum.module

data class UzumCallBack(
  var orderId: String? = null,
  var operationState: UzumOperationState? = null,
  var operationType: UzumOperationType? = null,
  var merchantOperationId: String? = null,
  val orderNumber: String? = null,
  val rrn: String? = null
) {
  override fun toString(): String {
    return "UzumCallBack(orderId=$orderId, operationState=$operationState, operationType=$operationType, merchantOperationId=$merchantOperationId, orderNumber=$orderNumber, rrn=$rrn)"
  }
}

data class UzumEventCallBack(
  var orderId: String? = null,
  var eventType: UzumEventType? = null,
  var actionCode: UzumOperationType? = null,
  var actionCodeDescription: String? = null,
  val orderNumber: String? = null,
)

data class UzumError(
  var orderId: String? = null,
  var actionCode: Int? = null,
  var actionCodeDescription: String? = null,
)

enum class UzumEventType {
  FORM_CLOSED
}

enum class UzumOperationState {
  SUCCESS,
  FAIL
}
