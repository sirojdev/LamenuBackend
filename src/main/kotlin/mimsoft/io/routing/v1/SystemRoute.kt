package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.features.manager_sys.routeToSysManager

fun Route.routeToSystem() {
  routeToSysAdmin()
  routeToSysManager()
}
