package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.board.routeToBoard
import mimsoft.io.client.user.routeToUser
import mimsoft.io.courier.routeToCouriers
import mimsoft.io.features.manager_sys.routeToSysManager
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.v1.*
import mimsoft.io.routing.v1.device.routeToDevice
import mimsoft.io.services.firebase.routeToFirebase
import mimsoft.io.waiter.routeToWaiter


fun Route.routeToV1() {
    routeToDevice()
    routeToSysAdmin()
    routeToSysManager()
    routeToCouriers()
    routeToMerchant()
    routeToWaiter()
    routeToBoard()
    routeToClient()
    routeToUser()
    routeToFiles()
    routeToIntegration()
    routeToOperator()
    routeToFirebase()
}