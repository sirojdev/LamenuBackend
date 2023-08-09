package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.client.user.routeToUser
import mimsoft.io.courier.routeToCouriers
import mimsoft.io.features.operator.routeToOperatorEntity
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.v1.*
import mimsoft.io.routing.v1.device.routeToDevice


fun Route.routeToV1() {
    routeToAdmin()
    routeToCouriers()
    routeToMerchant()
    routeToClient()
    routeToUser()
    routeToFiles()
    routeToDevice()
    routeToIntegration()
    routeToOperator()
}