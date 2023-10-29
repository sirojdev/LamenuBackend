package mimsoft.io.features.delivery

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToDelivery() {
  get("delivery") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val delivery = DeliveryService.get(merchantId = merchantId) ?: DeliveryDto()
    call.respond(delivery)
  }

  put("delivery") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val table = call.receive<DeliveryDto>()
    DeliveryService.add(table.copy(merchantId = merchantId))
    call.respond(HttpStatusCode.OK)
  }
}
