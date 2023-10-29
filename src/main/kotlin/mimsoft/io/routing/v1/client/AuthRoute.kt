package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig

fun Route.routeToClientAuth() {
  val userRepository: UserRepository = UserRepositoryImpl
  authenticate("device") { routeToSMS() }

  authenticate("modify") {
    post("sign-up") {
      val pr = call.principal<DevicePrincipal>()

      val user = call.receive<UserDto>()
      val result = userRepository.add(user.copy(phone = pr?.phone, merchantId = pr?.merchantId))
      if (result.isOk()) {
        DeviceController.updateCode(
          DeviceModel(
            id = pr?.id,
            merchantId = pr?.merchantId,
            action = "",
            code = null,
            phone = "",
            expAction = true
          )
        )
        val sessionUuid = generateSessionUUID(uuid = pr?.uuid, id = pr?.id)

        SessionRepository.auth(
          SessionTable(
            uuid = sessionUuid,
            merchantId = pr?.merchantId,
            deviceId = pr?.id,
            userId = result.body.toString().toLongOrNull()
          )
        )

        call.respond(
          user.copy(
            id = result.body.toString().toLongOrNull(),
            token = JwtConfig.generateUserToken(merchantId = pr?.merchantId, uuid = sessionUuid)
          )
        )
      } else {
        call.respond(result)
      }
    }
  }
}

fun generateSessionUUID(uuid: String?, id: Long?): String {
  return uuid + UUID.randomUUID() + "-" + id
}
