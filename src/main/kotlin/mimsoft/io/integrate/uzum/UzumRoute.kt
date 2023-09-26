package mimsoft.io.integrate.uzum

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.uzum.module.*

fun Route.routeToUzum() {
    route("uzum") {
        post("register") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            if (orderId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            call.respond(UzumService.register(orderId))
        }

        post("call-back") {
            val callBack = call.receive<UzumCallBack>()
            if (callBack.orderId == null || callBack.operationState == null || callBack.operationType == null || callBack.orderNumber == null) {
                call.respond(HttpStatusCode.BadRequest, "orderId or operationState or operationType is null ")
            }
            val uzumOrder = UzumRepository.getTransactionByUzumOrderId(callBack.orderId)
            if (uzumOrder?.uzumOrderId == null) {
                call.respond(HttpStatusCode.NoContent, "uzum order id not found")
            }

            if (callBack.operationState == UzumOperationState.FAIL) {
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
            if (uzumOrder?.orderId.toString() != callBack.orderNumber) {
                call.respond(HttpStatusCode.NoContent, "merchant order id not found")
            }
            when (callBack.operationType) {
                UzumOperationType.AUTHORIZE -> {
                    UzumService.authorize(callBack)
                }

                UzumOperationType.COMPLETE -> {
                    UzumService.completeTransaction(callBack)
                }

                UzumOperationType.REFUND -> {
                    UzumService.refund(callBack)
                }

                UzumOperationType.REVERSE -> {
                    UzumService.reverse(callBack)
                }

                else -> {}
            }
        }
        post("event-call-back") {
            val callBack = call.receive<UzumEventCallBack>()


        }
    }

}