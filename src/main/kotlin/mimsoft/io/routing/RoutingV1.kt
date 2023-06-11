package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.client.user.routeToUser
import mimsoft.io.routing.staff.routeToStaffApis
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.v1.routeToAdmin
import mimsoft.io.routing.v1.clientRouting
import mimsoft.io.routing.v1.routeToMerchant


fun Route.routeToV1() {
    routeToAdmin()
    routeToMerchant()
    clientRouting()
    routeToStaffApis()
    routeToUser()
    routeToFiles()
}