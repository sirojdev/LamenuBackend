package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.repository.OrderRepositoryImpl

fun Route.routeToClientOrderHistory() {
    route("order/history/get") {

    }
}