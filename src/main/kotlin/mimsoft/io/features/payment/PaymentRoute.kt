package mimsoft.io.features.payment

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToPayment(){


    get("payment"){
        val merchantId = 2L
        val payment = PaymentService.get(merchantId = merchantId)?: PaymentDto()
        call.respond(payment)
    }

    post ("payment"){
        val merchantId = 2L
        val table = call.receive<PaymentDto>()
        PaymentService.add(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

}