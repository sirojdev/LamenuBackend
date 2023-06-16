package mimsoft.io.features.order.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToOrderByCourierAndCollector() {
    val orderRepository: OrderRepository = OrderRepositoryImpl
    get("orders"){


        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val courierId = call.parameters["courierId"]
        if (courierId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val orders = orderRepository.getByCourierId(courierId = courierId, merchantId = merchantId)
        if (orders != null) {
            call.respond(HttpStatusCode.OK, orders)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

}