package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.features.client.routeToUser
import mimsoft.io.routing.staff.routeToStaffApis
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.admin.routeToAdmin
import mimsoft.io.routing.client.clientRouting
import mimsoft.io.routing.merchant.routeToMerchant


fun Route.routeToV1() {
    routeToAdmin()
    routeToMerchant()
    clientRouting()
    routeToStaffApis()
    routeToUser()
    routeToFiles()
}