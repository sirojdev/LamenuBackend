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

        post("callback") {
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
                call.respond(HttpStatusCode.NoContent, "merchant order id not found or status not equal")
            }
            when (callBack.operationType) {
                UzumOperationType.AUTHORIZE -> {
                    if (uzumOrder?.operationType!=UzumOperationType.TO_REGISTER){
                        call.respond(HttpStatusCode.NoContent,"this transaction not found or status not equal")
                    }else{
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId,UzumOperationType.AUTHORIZE)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
                }

                UzumOperationType.COMPLETE -> {
                    if (uzumOrder?.operationType!=UzumOperationType.TO_REGISTER){
                        call.respond(HttpStatusCode.NoContent,"this transaction not found or status not equal")
                    }else{
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId,UzumOperationType.AUTHORIZE)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
//                    UzumService.completeTransaction(callBack)
                }

                UzumOperationType.REFUND -> {
                    if (uzumOrder?.operationType!=UzumOperationType.COMPLETE){
                        call.respond(HttpStatusCode.NoContent,"this transaction not found or status not equal")
                    }else{
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId,UzumOperationType.REFUND)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
//                    UzumService.refundTransaction(callBack)
                }

                UzumOperationType.REVERSE -> {
                    if (uzumOrder?.operationType!=UzumOperationType.AUTHORIZE){
                        call.respond(HttpStatusCode.NoContent,"this transaction not found or status not equal")
                    }else{
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId,UzumOperationType.AUTHORIZE)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
//                    UzumService.reverseTransaction(callBack)
                }

                else -> {}
            }
        }
        post("event-callback") {
            val callBack = call.receive<UzumEventCallBack>()
        }
        post("refund") {
            val refund = call.receive<UzumRefund>()
            if (refund.orderId == null || refund.amount == null) {
                call.respond(HttpStatusCode.BadRequest, "orderId or amount is null ")
            }
            UzumService.refund(refund)
        }
        post("reverse") {
            val reverse = call.receive<UzumRefund>()
            if (reverse == null) {
                call.respond(HttpStatusCode.BadRequest, "orderId or amount is null ")
            }
            UzumService.reverse(reverse)
        }
    }

}