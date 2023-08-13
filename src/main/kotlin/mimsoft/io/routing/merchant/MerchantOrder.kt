package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal
/**
 * Created by Mimosa on 11/30/2020 at 12:00 PM
 */
fun Route.routeToOrderByCourierAndCollector() {
    val orderRepository: OrderRepository = OrderRepositoryImpl
    get("orders") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val courierId = call.parameters["courierId"]?.toLongOrNull()
        val collectorId = call.parameters["collectorId"]?.toLongOrNull()
        if (courierId == null && collectorId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val list = orderRepository.getBySomethingId(courierId = courierId, collectorId = collectorId, merchantId = merchantId)
        call.respond(HttpStatusCode.OK, list)
        return@get
    }
}