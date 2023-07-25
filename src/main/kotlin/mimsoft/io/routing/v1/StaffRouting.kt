package mimsoft.io.routing.staff

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.routeToStaffAuth
import mimsoft.io.features.staff.routeToStaffProfile

fun Route.routeToStaffApis() {
    route("staff") {
        routeToStaffAuth()
        authenticate ("staff"){
            routeToStaffProfile()
        }
    }
}