package mimsoft.io.courier

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.utils.authorize

fun Route.routeToCouriers() {
    routeToCourierAuth()
    authenticate("merchant") {
        routeToCouriersInfo()
    }
}