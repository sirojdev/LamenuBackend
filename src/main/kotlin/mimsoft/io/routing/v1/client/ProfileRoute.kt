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
import mimsoft.io.client.profile.ClientProfileService
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.sms.SmsService
import mimsoft.io.files.FilesService
import mimsoft.io.rsa.Generator
import mimsoft.io.services.sms.SmsSenderService
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToClientProfile() {
  val userRepository: UserRepository = UserRepositoryImpl
  route("profile") {
    get {
      val pr = call.principal<BasePrincipal>()
      val user = userRepository.get(id = pr?.userId, merchantId = pr?.merchantId)
      call.respond(user ?: HttpStatusCode.NoContent)
    }
    post("send-sms") {
      val pr = call.principal<BasePrincipal>()
      val phone = call.receive<DeviceModel>().phone
      if (phone == null) {
        call.respond(HttpStatusCode.BadRequest, "phone required")
        return@post
      }
      val merchantId = pr?.merchantId
      val result = SmsService.checkSmsTime2(phone = phone)
      if (result == null || result == "already_sent") {
        call.respond(HttpStatusCode.TooManyRequests)
        return@post
      } else {
        val gn = Generator.generate(true)
        SmsSenderService.send(merchantId = pr?.merchantId, phone, "code ${gn.code}")
        call.respond(
          DeviceModel(
            token =
              JwtConfig.generateTokenForUpdatePhone(
                merchantId = merchantId,
                uuid = pr?.uuid,
                hash = gn.hash,
                phone = phone,
                userId = pr?.userId!!
              )
          )
        )
      }
    }

    patch {
      val pr = call.principal<BasePrincipal>()
      val user = call.receive<UserDto>()
      call.respond(userRepository.update(user.copy(id = pr?.userId, merchantId = pr?.merchantId)))
    }

    put("firebase") {
      val pr = call.principal<BasePrincipal>()
      val device = call.receive<DeviceModel>()
      DeviceController.editFirebase(sessionUUID = pr?.uuid, token = device.firebaseToken)
      call.respond(HttpStatusCode.OK)
    }

    post("logout") {
      val pr = call.principal<BasePrincipal>()
      SessionRepository.expire(pr?.uuid)
      call.respond(HttpStatusCode.OK)
    }

    delete {
      val pr = call.principal<BasePrincipal>()
      userRepository.delete(id = pr?.userId, merchantId = pr?.merchantId)
      call.respond(HttpStatusCode.OK)
    }
    post("image") {
      val user = call.receive<UserDto>()
      val principal = call.principal<BasePrincipal>()
      if (user.image == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }
      val rs = ClientProfileService.updateImage(user.image, principal)
      if (rs.httpStatus == HttpStatusCode.OK) {
        call.respond(rs.body)
      } else {
        call.respond(rs.httpStatus)
      }
    }
    delete("image") {
      val principal = call.principal<BasePrincipal>()
      val user = userRepository.get(principal?.userId, principal?.merchantId)
      if (user == null) {
        call.respond(HttpStatusCode.NotFound, "user not found")
        return@delete
      }
      userRepository.updateImage(null, principal?.userId)
      if (user.image != null) FilesService.deleteFile(user.image)
      call.respond(user.copy(image = null))
    }
  }
}