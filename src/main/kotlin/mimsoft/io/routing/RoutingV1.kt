package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.client.user.routeToUser
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.staff.routeToStaffApis
import mimsoft.io.routing.v1.device.routeToDevice
import mimsoft.io.routing.v1.routeToAdmin
import mimsoft.io.routing.v1.routeToClient
import mimsoft.io.routing.v1.routeToMerchant


fun Route.routeToV1() {
    routeToAdmin()
    routeToMerchant()
    routeToClient()
    routeToStaffApis()
    routeToUser()
    routeToFiles()
    routeToDevice()
}