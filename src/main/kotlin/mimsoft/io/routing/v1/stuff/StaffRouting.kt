package mimsoft.io.routing.v1.stuff

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.routeToStaffProfile

fun Route.routeToStaffApis() {
  route("staff") {
    routeToStaffAuth()
    authenticate("staff") {
      routeToStaffDevice()
      routeToStaffProfile()
      staffAuthRoute2()
    }
  }
}
