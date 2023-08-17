package mimsoft.io.features.merchant.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.favourite.merchant
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToMerchantOrder() {

    val orderService: OrderRepository = OrderRepositoryImpl

    authenticate("merchant") {
        get("orders") {
            val principal = call.principal<BasePrincipal>()
            val search = call.parameters["search"]

            val orders = orderService.getAll(merchantId = principal?.merchantId)
            if (orders==null || orders.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(orders)
        }

        get("orders/{id}") {
            call.attributes
            val principal = call.principal<BasePrincipal>()
            val id = call.parameters["id"]?.toLongOrNull()
            if (id==null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val order = orderService.get(id)
        }

        post("order") {

        }

        delete("order/{id}") {

        }

    }
}