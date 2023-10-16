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
import mimsoft.io.features.operator.OperatorService
import mimsoft.io.features.operator.chat.routeToOperatorChat
import mimsoft.io.features.order.routeToOrder
import mimsoft.io.features.promo.routeToPromo
import mimsoft.io.features.sms.routeToSms
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.routeToCollector
import mimsoft.io.routing.merchant.routeToUserUser
import mimsoft.io.utils.principal.BasePrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToOperator() {
    val log: Logger = LoggerFactory.getLogger("routeToOperator")
    route("operator") {
        post("auth") {
            val staff = call.receive<StaffDto>()

            StaffService.auth(staff).let {
                log.info("auth: $it")
                call.respond(it.httpStatus, it.body)
            }
        }
        authenticate("operator") {
            route("profile") {
                get {
                    val principal = call.principal<BasePrincipal>()

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
            routeToOperatorChat()
            routeToOrder()
            routeToCourier()
            routeToCollector()
        }
    }
}