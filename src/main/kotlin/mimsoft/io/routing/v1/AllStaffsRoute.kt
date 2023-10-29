package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.courier.routeToCouriers
import mimsoft.io.waiter.routeToWaiter

fun Route.routeToStaffs() {
  routeToCouriers()
  routeToWaiter()
  routeToOperator()
}
