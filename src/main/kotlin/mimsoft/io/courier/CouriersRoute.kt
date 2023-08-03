package mimsoft.io.courier

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.courier.transaction.routeToCourierTransaction
import mimsoft.io.utils.authorize

fun Route.routeToCouriers() {
    authenticate("device") {
        routeToCourierAuth()
    }
    authenticate("staff") {
        route("courier") {
            routeToCouriersInfo()
            authenticate("staff") {
                routeToCouriersInfo()
            }
            routeToCourierTransaction()
        }

        }
    }
}