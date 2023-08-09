package mimsoft.io.features.operator

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.staff.StaffService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig

fun Route.routeToOperatorEntity() {

    post("operator/auth") {
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


        get("operators") {
            val principal = call.principal<StaffPrincipal>()
            val merchantId = principal?.merchantId
            if (merchantId == null) {
                call.respond(HttpStatusCode.BadRequest, "merchantId must not be null")
                return@get
            }
            OperatorService.getAll(merchantId).let {
                call.respond(it)
            }
        }

        get("operator/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id must not be null")
                return@get
            }
            OperatorService.get(id).let {
                call.respond(it ?: HttpStatusCode.NoContent)
            }
        }

        post("operator") {
            val operator = call.receive<Operator>()
            OperatorService.add(operator).let {
                if (it.body == null) call.respond(it.httpStatus)
                else call.respond(it.httpStatus, it.body)
            }
        }

        put("operator") {
            val operator = call.receive<Operator>()
            OperatorService.update(operator).let {
                if (it.body == null) call.respond(it.httpStatus)
                else call.respond(it.httpStatus, it.body)
            }
        }

        delete("operator/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id must not be null")
                return@delete
            }
            OperatorService.delete(id).let {
                call.respond(it)
            }
        }
    }
}