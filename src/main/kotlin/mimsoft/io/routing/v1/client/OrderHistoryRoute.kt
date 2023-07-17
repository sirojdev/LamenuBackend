package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper

fun Route.routeToClientOrderHistory() {
    route("order/history/get") {
        get {
            val pr = call.principal<UserPrincipal>()
            val response: Any
            val clientId = pr?.id
            val merchantId = pr?.merchantId
            val filter = call.parameters["filter"].toString()
            if(filter == null){
                    response = OrderRepositoryImpl.getClientOrders(clientId = clientId, merchantId = merchantId)
            } else response = OrderRepositoryImpl.getClientOrders(clientId = clientId, merchantId = merchantId, filter = filter)

            if (response.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }
    }
}