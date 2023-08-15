package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.client.user.routeToUser
import mimsoft.io.courier.merchantChat.merchantChatRoute
import mimsoft.io.courier.routeToCouriers
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.v1.*
import mimsoft.io.routing.v1.device.routeToDevice
import mimsoft.io.waiter.routeToWaiter


fun Route.routeToV1() {
    routeToDevice()
    routeToAdmin()
    merchantChatRoute()
    routeToCouriers()
    routeToMerchant()
    routeToWaiter()
    routeToClient()
    routeToUser()
    routeToFiles()
    routeToIntegration()
    routeToOperator()
}