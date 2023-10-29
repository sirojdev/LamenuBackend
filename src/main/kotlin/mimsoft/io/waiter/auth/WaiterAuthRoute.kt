package mimsoft.io.waiter.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.client.device.DeviceType
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.ItemNotFoundException
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.ResponseData
import mimsoft.io.waiter.WaiterService

fun Route.routeToWaiterAuth() {
  val waiterService = WaiterService
  val sessionRepo = SessionRepository
  route("waiter") {
    route("device") {
      post {
        val device: DeviceModel = call.receive()
        val appKey = call.parameters["appKey"]?.toLongOrNull()
        if (
          device.brand == null ||
            device.model == null ||
            device.build == null ||
            device.osVersion == null ||
            device.uuid.isNullOrBlank() ||
            appKey == null
        ) {
          throw BadRequest("error input,brand,model,build,osVersion,uuid,appKey required")
        } else {
          val ip = call.request.host()
          val result =
            DeviceController.auth(
              device.copy(
                ip = ip,
                merchantId = appKey,
                appKey = appKey,
                deviceType = DeviceType.WAITER
              )
            )
          call.respond(ResponseData(data = result))
        }
      }
    }

    authenticate("device") {
      post("auth") {
        val device = call.principal<DevicePrincipal>()
        val waiter = call.receive<StaffDto>()
        if (waiter.password.isNullOrBlank() || waiter.phone.isNullOrBlank()) {
          throw BadRequest("phone or password is null")
        }
        val staff = waiterService.auth(waiter.copy(merchantId = device?.merchantId))
        if (staff == null) throw ItemNotFoundException("phone or password is incorrect")
        else {
          val uuid = waiterService.generateUuid(staff.id)
          sessionRepo.auth(
            SessionTable(
              uuid = uuid,
              stuffId = staff.id,
              merchantId = staff.merchantId,
              deviceId = device?.id,
              role = DeviceType.WAITER?.name
            )
          )

          call.respond(
            ResponseData(
              data =
                JwtConfig.generateWaiterToken(
                  staffId = staff.id,
                  merchantId = staff.merchantId,
                  uuid = uuid,
                  branchId = staff.branchId
                )
            )
          )
        }
      }
    }

    authenticate("waiter") {
      post("logout") {
        val principal = getPrincipal()
        val response = WaiterService.logout(principal?.uuid)
        call.respond(response)
      }
    }
  }
}
