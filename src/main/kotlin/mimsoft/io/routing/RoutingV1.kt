package mimsoft.io.routing

import io.ktor.server.routing.*
import mimsoft.io.board.routeToBoard
import mimsoft.io.client.user.routeToUser
import mimsoft.io.files.routeToFiles
import mimsoft.io.routing.v1.*
import mimsoft.io.routing.v1.branch.routeToBranchAdmin
import mimsoft.io.routing.v1.device.routeToDevice
import mimsoft.io.services.firebase.routeToFirebase


fun Route.routeToV1() {
    routeToDevice()
    routeToSystem()
    routeToStaffs()
    routeToMerchantAdmin()
    routeToBranchAdmin()
    routeToBoard()
    routeToClient()
    routeToFiles()
    routeToIntegration()
    routeToFirebase()
}