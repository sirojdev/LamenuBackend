package mimsoft.io.waiter.info

import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.files.FilesService
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.InternalServerException
import mimsoft.io.utils.plugins.ItemNotFoundException
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.ResponseData
import mimsoft.io.validation.bindJson
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
        val dto = call.bindJson<WaiterUpdateRequest>()
        val result = waiterService.updateWaiterProfile(dto.copy(id = principal?.staffId))
        call.respond(ResponseData(data = result))
    }
    patch("update/image") {
        val principal = getPrincipal()
        val staff = waiterService.getWithPasswordAndImages(principal?.staffId)
        val result = FilesService.uploadFile(
            multipart = call.receiveMultipart()
        )
        if (result.isBlank()) throw InternalServerException("cannot upload image")
        if (WaiterService.updateWaiterImageProfile(
                result,
                principal?.staffId ?: -1
            )
        ) FilesService.deleteFile(staff?.image ?: "")
        call.respond(ResponseData(data = result))
    }
    patch("update/password") {
        val principal = getPrincipal()
        val dto = call.bindJson<WaiterUpdatePasswordRequest>()
        if (dto.newPassword != dto.newConfirmPassword) throw BadRequest("new password and confirm password not equal")
        val staff = waiterService.getWithPasswordAndImages(principal?.staffId)
            ?: throw ItemNotFoundException("waiter not found")
        if (staff.password != dto.oldPassword) throw BadRequest("password incorrect")
        val result = waiterService.updateWaiterPassword(dto.copy(id = principal?.staffId))
        call.respond(ResponseData(data = result))
    }
}