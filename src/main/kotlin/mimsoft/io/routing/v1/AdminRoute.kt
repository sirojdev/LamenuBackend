package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.routing.v1.admin.merchantRoute

fun Route.routeToAdmin()  {
    route("admin"){
        merchantRoute()
    }

}