package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.notification.repository.NotificationRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal


fun Route.routeToNotification() {
    get("notification") {
        val user = call.principal<BasePrincipal>()
        val merchantId = user?.merchantId
        val userId = user?.userId
        val result = NotificationRepositoryImpl.getClient(merchantId = merchantId, userId = userId)
        if(result.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, result)
        return@get
    }
}