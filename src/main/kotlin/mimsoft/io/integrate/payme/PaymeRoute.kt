package mimsoft.io.integrate.payme

import com.google.gson.Gson
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

            println("\nreceive: $receive")

            receive.let {

                val response = when (it.method) {

                    CHECK_PERFORM_TRANSACTION -> {
                        paymeService.checkPerform(
                            account = Gson().fromJson(params["account"].toString(), Account::class.java),
                            amount = params["amount"] as Double,
                            transactionId = receive.id,
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
    }

}