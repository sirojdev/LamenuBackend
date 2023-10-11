package mimsoft.io.waiter.info

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.files.FilesService
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.ItemNotFoundException
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.ResponseData
import mimsoft.io.waiter.WaiterService

fun Route.routeToWaitersInfo() {
    val waiterService = WaiterService
    get("profile") {
        val principal = getPrincipal()
        val waiterId = principal?.staffId
        val dto = waiterService.getById(waiterId) ?: throw ItemNotFoundException("waiter not found")
        call.respond(ResponseData(data = dto))
    }

    post("firebase") {
        val principal = getPrincipal()
        val firebase = call.parameters["firebase"]
        val session = SessionRepository.get(principal?.uuid)
        call.respond(
            ResponseData(
                data = DeviceController.editFirebaseWithDeviceId(
                    deviceId = session?.deviceId,
                    token = firebase
                )
            )
        )
    }

    patch("update") {
        val principal = getPrincipal()
        val dto = call.receive<StaffDto>()
        val staff = waiterService.getWithPasswordAndImages(principal?.staffId)
            ?: throw ItemNotFoundException("waiter not found")
        if (dto.newPassword != null) if (staff.password != dto.password) throw BadRequest("Error in the old password")
        val result = waiterService.updateWaiterProfile(dto.copy(id = principal?.staffId))
        if (dto.image != null && result) staff.image?.let { it1 -> FilesService.deleteFile(it1) }
        call.respond(ResponseData(data = result))
    }
}