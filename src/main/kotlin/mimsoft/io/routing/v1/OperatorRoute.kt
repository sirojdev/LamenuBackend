package mimsoft.io.routing.v1

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.routeToCourier
import mimsoft.io.features.message.routeToMessage
import mimsoft.io.features.notification.routeToNotification
import mimsoft.io.features.operator.Operator
import mimsoft.io.features.operator.OperatorService
import mimsoft.io.features.operator.routeToOperatorEntity
import mimsoft.io.features.order.routeToOrder
import mimsoft.io.features.promo.routeToPromo
import mimsoft.io.features.sms.routeToSms
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.routeToCollector
import mimsoft.io.routing.merchant.routeToUserUser
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig

fun Route.routeToOperator() {


    route("operator") {

        post("auth") {
            val staff = call.receive<StaffDto>()

            StaffService.auth(staff).let {
                if (it.body == null) {
                    call.respond(it.httpStatus)
                    return@post
                }

                val body = it.body as StaffDto

                val uuid = SessionRepository.generateUuid()

                SessionRepository.add(
                    SessionTable(
                        uuid = uuid,
                        merchantId = body.merchantId,
                        phone = body.phone,
                        stuffId = body.id,
                        role = "operator",
                        isExpired = false
                    )
                )

                call.respond(mapOf("token" to JwtConfig.generateOperatorToken(body.merchantId, uuid)))
            }
        }


        authenticate("operator") {

            route("profile") {

                get {
                    val principal = call.principal<StaffPrincipal>()

                    OperatorService.get(principal?.staffId).let {
                        call.respond(it ?: HttpStatusCode.NoContent)
                    }
                }

                put {
                    val principal = call.principal<StaffPrincipal>()
                    val operator = OperatorService.get(principal?.staffId)
                    OperatorService.update(operator?.operator).let {
                        if (it.body == null) call.respond(it.httpStatus)
                        else call.respond(it.httpStatus, it.body)
                    }
                }
            }

            routeToSms()
            routeToMessage()
            routeToPromo()
            routeToUserUser()
            routeToNotification()

            routeToOrder()
            routeToCourier()
            routeToCollector()
        }
    }
}