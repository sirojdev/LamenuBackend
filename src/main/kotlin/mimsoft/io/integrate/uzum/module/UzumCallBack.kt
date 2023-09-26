package mimsoft.io.integrate.uzum.module

data class UzumCallBack(
    var orderId: String? = null,
    var operationState: UzumOperationState? = null,
    var operationType: UzumOperationType? = null,
    var merchantOperationId: String? = null,
    val orderNumber:String?=null,
    val rrn:String?=null
)

data class UzumEventCallBack(
    var orderId: String? = null,
    var eventType: UzumEventType? = null,
    var actionCode: UzumOperationType? = null,
    var actionCodeDescription: String? = null,
    val orderNumber:String?=null,
)
enum class UzumEventType{
    FORM_CLOSED
}

enum class UzumOperationState{
    SUCCESS,
    FAIL
}