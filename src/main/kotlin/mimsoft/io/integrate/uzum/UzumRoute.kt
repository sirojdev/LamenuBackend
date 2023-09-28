package mimsoft.io.integrate.uzum

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.join_poster.JoinPosterService
import mimsoft.io.integrate.uzum.module.*
import org.apache.commons.logging.Log
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToUzum() {
    val log: Logger = LoggerFactory.getLogger(UzumService::class.java)

    route("uzum") {
        post("register") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            log.info("inside register")
            if (orderId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            call.respond(UzumService.register(orderId))
        }

        post("event-callback") {
            val callBack = call.receive<UzumCallBack>()
            log.info("INSIDI callback  $callBack")

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
                    if (uzumOrder?.operationType != UzumOperationType.TO_REGISTER) {
                        call.respond(HttpStatusCode.NoContent, "this transaction not found or status not equal")
                    } else {
                        log.info("inside AUTHORIZE")
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId, UzumOperationType.AUTHORIZE)
                        UzumService.complete(uzumOrder)
                        call.respond(HttpStatusCode.OK)
                    }
                }

                UzumOperationType.COMPLETE -> {
                    log.info("inside COMPLETE")

                    if (uzumOrder?.operationType != UzumOperationType.AUTHORIZE) {
                        call.respond(HttpStatusCode.NoContent, "this transaction not found or status not equal")
                    } else {
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId, UzumOperationType.COMPLETE)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
//                    UzumService.completeTransaction(callBack)
                }

                UzumOperationType.REFUND -> {
                    log.info("inside REFUND")

                    if (uzumOrder?.operationType != UzumOperationType.COMPLETE) {
                        call.respond(HttpStatusCode.NoContent, "this transaction not found or status not equal")
                    } else {
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId, UzumOperationType.REFUND)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
//                    UzumService.refundTransaction(callBack)
                }

                UzumOperationType.REVERSE -> {
                    log.info("inside REVERSE")
                    if (uzumOrder?.operationType != UzumOperationType.AUTHORIZE) {
                        call.respond(HttpStatusCode.NoContent, "this transaction not found or status not equal")
                    } else {
                        UzumRepository.updateOperationType(uzumOrder.uzumOrderId, UzumOperationType.AUTHORIZE)
//                        UzumService.authorizeTransaction(callBack)
                        call.respond(HttpStatusCode.OK)
                    }
//                    UzumService.reverseTransaction(callBack)
                }

                else -> {}
            }
        }
//        post("event-callback") {
//            val callBack = call.receive<UzumEventCallBack>()
//            log.info("event-callback  $callBack")
//            UzumRepository.saveLog(callBack)
//            call.respond(HttpStatusCode.OK)
//        }
        post("refund") {
            // TOLOVNI QAYTARISH
            log.info("refund  refund")
            val merchantOrderId = call.parameters["orderId"]?.toLongOrNull()
//            val refund = call.receive<UzumRefund>()
//            if (refund.orderId == null) {
//                call.respond(HttpStatusCode.BadRequest, "orderId  is null ")
//            }
            val uzumOrder = UzumRepository.getTransactionByMerchantOrderId(merchantOrderId)
            if (uzumOrder == null) {
                call.respond(HttpStatusCode.BadRequest, " order not found")
            }
            if (uzumOrder?.operationType == UzumOperationType.COMPLETE) {
                call.respond(HttpStatusCode.MethodNotAllowed, "order operation type not complete")
            }
            println("iinside before refund ")

            call.respond(
                UzumService.refund(
                    UzumRefund(orderId = uzumOrder?.uzumOrderId, amount = uzumOrder?.price),
                    uzumOrder
                )
            )
        }
        post("reverse") {
            println("inside reverse ")

            val reverse = call.receive<UzumRefund>()
            if (reverse == null) {
                call.respond(HttpStatusCode.BadRequest, "orderId or amount is null ")
            }
            call.respond(UzumService.reverse(reverse))
        }
    }

}