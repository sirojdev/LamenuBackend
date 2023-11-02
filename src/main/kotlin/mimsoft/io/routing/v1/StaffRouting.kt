package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.features.staff.routeToStaffAuth

fun Route.routeToStaffApis() {
  route("staff") { routeToStaffAuth() }
}
