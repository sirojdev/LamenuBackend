package mimsoft.io.features.manager_sys

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.routing.v1.sys_admin.merchantRoute
import mimsoft.io.routing.v1.sys_admin.routeToMerchantBot

fun Route.routeToSysManager() {
  route("manager") {
    routeToManagerAuth()
    routeToMerchantBot()
    authenticate("manager") {
      routeToManagerProfile()
      merchantRoute()
    }
  }
}
