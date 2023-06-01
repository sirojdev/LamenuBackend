package mimsoft.io.entities.delivery

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToDelivery(){
    get("delivery"){
        val merchantId = 1L
        val delivery = DeliveryService.get(merchantId = merchantId)?: DeliveryDto()
        call.respond(delivery)
    }

    post ("delivery"){
        val merchantId = 1L
        val table = call.receive<DeliveryDto>()
        DeliveryService.add(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }
}