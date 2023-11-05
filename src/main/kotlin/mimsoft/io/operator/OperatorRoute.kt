package mimsoft.io.operator

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.courier.routeToCourier
import mimsoft.io.features.message.routeToMessage
import mimsoft.io.features.notification.routeToNotification
import mimsoft.io.features.operator.OperatorService
import mimsoft.io.features.operator.chat.routeToOperatorChat
import mimsoft.io.features.promo.routeToPromo
import mimsoft.io.features.sms.routeToSms
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.routeToCollector
import mimsoft.io.operator.client.routeToOperatorClientRoute
import mimsoft.io.routing.merchant.routeToUserUser
import mimsoft.io.utils.plugins.getPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToOperator() {
  val log: Logger = LoggerFactory.getLogger("routeToOperator")
  route("operator") {
    post("auth") {
      val staff = call.receive<StaffDto>()

      val response = StaffService.authOperator(staff)
      if (response == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }
      call.respond(response)
    }

    authenticate("operator") {
      route("profile") {
        get {
          val principal = getPrincipal()
          OperatorService.get(principal?.staffId).let {
            call.respond(it ?: HttpStatusCode.NoContent)
          }
        }

        put {
          val pr = getPrincipal()
          val dto = call.receive<StaffDto>()
          if (dto.password != null && dto.newPassword != null) {
            val operator =
              StaffService.get(
                id = pr?.staffId,
                merchantId = pr?.merchantId,
                password = dto.password
              )
            if (operator == null) {
              call.respond(status = HttpStatusCode.BadRequest, message = "Wrong password")
              return@put
            }
          }
          val response = StaffService.update(dto.copy(id = pr?.staffId))
          call.respond(response)
        }

        post("logout") {
          val pr = getPrincipal()
          val response = OperatorService.logout(pr?.uuid)
          call.respond(response)
        }
      }

      get("findUserByPhone") {
        val pr = getPrincipal()
        val phone = call.parameters["phone"]
        val response = UserRepositoryImpl.search(phone = phone, merchantId = pr?.merchantId)
        if (response.data?.isEmpty() == true) {
          call.respond(message = "User not found", status = HttpStatusCode.NotFound)
          return@get
        }
        call.respond(response)
      }

      routeToSms()
      routeToMessage()
      routeToPromo()
      routeToUserUser()
      routeToNotification()
      routeToOperatorChat()
      routeToOrderOperator()
      routeToCourier()
      routeToCollector()
      routeToOperatorClientRoute()
    }
  }
}
