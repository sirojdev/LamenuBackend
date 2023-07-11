package mimsoft.io.routing.v1.client

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.staff.AdminPrincipal

fun Route.routeToNotification() {
    authenticate("admin", "user", optional = true) {
        route("test") {
            get() {
                val user = call.principal<UserPrincipal>()
                val admin = call.principal<AdminPrincipal>()

            }
        }
    }

}