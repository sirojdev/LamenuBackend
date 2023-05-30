package mimsoft.io.entities.payment

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToPayment(){
    val paymentMapper = PaymentMapper
    get("payments"){
        val payments = PaymentService.getAll().map {paymentMapper.toPaymentDto(it)}
        if(payments.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(payments)
    }

    get("payment/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val payment = PaymentService.get(id)
        if(payment == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(payment)
    }

    post ("payment"){
        val table = call.receive<PaymentDto>()
        PaymentService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("payment"){
        val table = call.receive<PaymentDto>()
        PaymentService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("payment/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        PaymentService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}