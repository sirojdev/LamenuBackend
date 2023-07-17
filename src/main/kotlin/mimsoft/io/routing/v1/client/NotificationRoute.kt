package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.notification.repository.NotificationRepositoryImpl


fun Route.routeToNotification() {
//    authenticate("user", optional = true) {
    get("notification") {
        val user = call.principal<UserPrincipal>()
        val merchantId = user?.merchantId
        val userId = user?.id
        val result = NotificationRepositoryImpl.getClient(merchantId = merchantId, userId = userId)
        if(result.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, result)
        return@get
    }
//    }

}