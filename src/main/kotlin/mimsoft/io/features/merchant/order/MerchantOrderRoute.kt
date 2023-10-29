package mimsoft.io.features.merchant.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToMerchantOrder() {

  val orderService = OrderService

  authenticate("merchant") {
    get("orders") {
      val principal = call.principal<BasePrincipal>()
      val search = call.parameters["search"]
      val orders = orderService.getAll(mapOf("merchantId" to principal?.merchantId as Any))
      call.respond(orders.httpStatus, orders.body)
    }

    get("orders/{id}") {
      val principal = call.principal<BasePrincipal>()
      val merchantId = principal?.merchantId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val order = orderService.get(id = id, merchantId = merchantId)
      call.respond(order)
    }
  }
}
