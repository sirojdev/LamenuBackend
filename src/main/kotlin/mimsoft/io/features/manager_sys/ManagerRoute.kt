package mimsoft.io.features.manager_sys

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.routing.v1.sys_admin.merchantRoute

fun Route.routeToSysManager() {
  route("manager") {
    routeToManagerAuth()
    authenticate("manager") {
      routeToManagerProfile()
      merchantRoute()
    }
  }
}
