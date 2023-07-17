package mimsoft.io.integrate.payme

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.payme.models.Account
import mimsoft.io.integrate.payme.models.Receive
import mimsoft.io.integrate.payme.models.Receive.Companion.CANCEL_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.CHECK_PERFORM_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.CHECK_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.CREATE_TRANSACTION
import mimsoft.io.integrate.payme.models.Receive.Companion.GET_STATEMENT
import mimsoft.io.integrate.payme.models.Receive.Companion.PERFORM_TRANSACTION

fun Route.routeToPayme() {

    authenticate("payme") {
        post("payment/payme") {

            val merchantId = call.parameters["merchantId"]?.toLongOrNull()
            val receive: Receive = call.receive()
            val paymeService = PaymeService
            val params = receive.params

            receive.let {

                val response = when (it.method) {

                    CHECK_PERFORM_TRANSACTION -> {
                        paymeService.checkPerform(
                            account = params["account"] as Account,
                            amount = params["amount"] as Long,
                            transactionId = receive.id,
                        )
                    }

                    CREATE_TRANSACTION -> {
                        paymeService.createTransaction(
                            paycomId = params["id"] as String,
                            account = params["account"] as Account,
                            amount = params["amount"] as Long,
                            pacomTime = params["time"] as Long,
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
                            reason = params["reason"] as Int,
                            transactionId = receive.id,
                        )
                    }
                    CHECK_TRANSACTION -> {
                        paymeService.checkTransaction(
                            paycomId = params["id"] as String
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
    }

}