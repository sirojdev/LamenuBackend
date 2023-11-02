package mimsoft.io.routing.v1.device

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel

fun Route.routeToDevice() {
  post("device") {
    val device: DeviceModel = call.receive()
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
