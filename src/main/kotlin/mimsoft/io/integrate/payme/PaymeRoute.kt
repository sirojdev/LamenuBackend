package mimsoft.io.integrate.payme

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.integrate.payme.models.*
import mimsoft.io.integrate.payme.models.Receive.Companion.CANCEL_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.CHECK_PERFORM_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.CHECK_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.CREATE_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.GET_STATEMENT
import mimsoft.io.integrate.payme.models.Receive.Companion.PERFORM_TRANSACTION
import mimsoft.io.utils.plugins.GSON
import java.util.Base64

fun Route.routeToPayme() {

    val paymeService = PaymeService
    val payment = PaymentService

    post("payment/payme/{merchantId}") {
        val merchantId = call.parameters["merchantId"]?.toLongOrNull()
        val receive: Receive = call.receive()

        val authorizationHeader = call.request.headers["Authorization"]?.substringAfter("Basic ")
        if (authorizationHeader == null) {
            call.respond(
                HttpStatusCode.OK,
                ErrorResult(
                    error = Error(
                        code = -32504,
                        message = Message.WRONG_HEADERS
                    ),
                    id = receive.id
                )
            )
            return@post
        }

        val decodedCredentials = String(Base64.getDecoder().decode(authorizationHeader)).split(":")
        val login = decodedCredentials.getOrElse(0) { "" }
        val password = decodedCredentials.getOrElse(1) { "" }

        val paymentCredential = payment.paymeVerify(password)
        if (login != "Paycom" || paymentCredential == null) {
            call.respond(
                HttpStatusCode.OK,
                ErrorResult(
                    error = Error(
                        code = -32504,
                        message = Message.WRONG_HEADERS
                    ),
                    id = receive.id
                )
            )
            return@post
        }

        val params = receive.params

        println("\nreceive: $receive")

        receive.let {

            val response = when (it.method) {

                CHECK_PERFORM_TRANSACTION -> {
                    paymeService.checkPerform(
                        account = Gson().fromJson(params["account"].toString(), Account::class.java),
                        amount = params["amount"] as Double,
                        transactionId = receive.id,
                        merchantId = merchantId,
                    )
                }

                CREATE_TRANSACTION -> {
                    paymeService.createTransaction(
                        paycomId = params["id"] as String,
                        account = Gson().fromJson(params["account"].toString(), Account::class.java),
                        amount = params["amount"] as Double,
                        pacomTime = params["time"] as Double,
                        transactionId = receive.id,
                    )
                }

                PERFORM_TRANSACTION -> {
                    paymeService.performTransaction(
                        paycomId = params["id"] as String,
                        transactionId = receive.id,
                    )
                }

                CANCEL_TRANSACTION -> {
                    paymeService.cancelTransaction(
                        paycomId = params["id"] as String,
                        reason = params["reason"] as Double,
                        transactionId = receive.id,
                    )
                }

                CHECK_TRANSACTION -> {
                    paymeService.checkTransaction(
                        paycomId = params["id"] as String,
                        transactionId = receive.id,
                    )
                }

                GET_STATEMENT -> {
                    paymeService.getStatement(
                        from = params["from"] as Long,
                        to = params["to"] as Long
                    )
                }

                else -> {
                    null
                }
            }
            call.respond(response as Any)
        }
    }

    get("payment/payme/{merchantId}/{orderId}/{amount}") {
        val merchantId = call.parameters["merchantId"]?.toLongOrNull()
        val orderId = call.parameters["orderId"]?.toLongOrNull()
        val amount = call.parameters["amount"]?.toIntOrNull()

        if (merchantId == null || orderId == null || amount == null) {
            call.respond(HttpStatusCode.BadRequest, "merchantId, orderId, amount must be not null")
            return@get
        }

        val response = paymeService.getCheckout(orderId, amount, merchantId)
        call.respond(response)
    }

}