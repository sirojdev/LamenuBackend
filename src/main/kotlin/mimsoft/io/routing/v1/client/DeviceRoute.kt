package mimsoft.io.routing.v1.client

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.session.SessionRepository

fun Route.routeToClientDevice() {
  route("device") {
    post {
      val device: DeviceModel = call.receive()
      val merchantId = call.parameters["appKey"]?.toLongOrNull()
      device.merchantId = merchantId
      println(Gson().toJson(device))
      if (
        device.brand == null ||
          device.model == null ||
          device.build == null ||
          device.osVersion == null ||
          device.uuid.isNullOrBlank() ||
          device.merchantId == null
      ) {

        call.respond(HttpStatusCode.BadRequest, "error input")
      } else {
        val ip = call.request.host()
        val result = DeviceController.auth(device.copy(ip = ip))
        call.respond(result)
      }
    }
  }

  authenticate("user") {
    route("device") {
      get {
        val pr = call.principal<UserPrincipal>()
        val devices =
          SessionRepository.getUserDevices(
            userId = pr?.id,
            merchantId = pr?.merchantId,
            uuid = pr?.uuid
          )
        call.respond(devices)
      }
      /** terminate single device */
      post("terminate") {
        val id = call.parameters["id"]?.toLongOrNull()
        val pr = call.principal<UserPrincipal>()
        SessionRepository.expireOther(
          uuid = pr?.uuid,
          merchantId = pr?.merchantId,
          userId = pr?.id,
          deviceId = id
        )
        call.respond(HttpStatusCode.OK)
      }
      /** terminate other devices */
      post("terminate/others") {
        val id = call.parameters["id"]?.toLongOrNull()
        val pr = call.principal<UserPrincipal>()
        SessionRepository.expireOthers(
          uuid = pr?.uuid,
          merchantId = pr?.merchantId,
          userId = pr?.id,
        )
        call.respond(HttpStatusCode.OK)
      }
    }
  }
}
