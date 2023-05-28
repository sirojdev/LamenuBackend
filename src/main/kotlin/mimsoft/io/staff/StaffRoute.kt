package mimsoft.io.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.*

fun Route.routeToStaff() {

    val staffService = StaffService
    val sessionRepo = SessionRepository

    post("staff/auth") {
        val staff = call.receive<StaffDto>()
        val status = staffService.auth(staff)

        if (status.httpStatus != OK)
            call.respond(status.httpStatus, status)
        else {
            val authStaff = status.body as StaffDto?
            val uuid = staffService.generateUuid(authStaff?.id)
//            val roles = roleService.getByStaff(staffBody?.id)

            sessionRepo.auth(
                SessionTable(
                    uuid = uuid,
                    stuffId = authStaff?.id,
                )
            )

            call.respond(
                authStaff?.copy(
                    token = JwtConfig.generateAccessToken(
                        entityId = authStaff.id,
                        forUser = false,
                        uuid = uuid,
//                        roles = roles
                    )
                ) ?: HttpStatusCode.NoContent
            )
        }

    }



    get("staffs") {
        val staffs = staffService.getAll()
        call.respond(staffs.ifEmpty { HttpStatusCode.NoContent })
    }

    get("staff") {
        val id = call.parameters["id"]?.toLongOrNull()

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val staff = staffService.get(id)
        if (staff == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(staff)
    }


    post("staff") {
        val principal = call.principal<LaPrincipal>()
        val staff = call.receive<StaffDto>()

        val statusTimestamp = timestampValidator(staff.birthDay)

        if (statusTimestamp.httpStatus != OK) {
            call.respond(statusTimestamp)
            return@post
        }

        val status = staffService.add(staff)
        val body = status.body as StaffDto?
        call.respond(status.httpStatus, body?.id?:0)


    }

    put("staff") {
        val principal = call.principal<LaPrincipal>()
        val staff = call.receive<StaffDto>()

        val statusTimestamp = timestampValidator(staff.birthDay)

        if (statusTimestamp.httpStatus != OK) {
            call.respond(statusTimestamp)
            return@put
        }

        val status = staffService.update(staff)

        val body = status.body as StaffDto?
        call.respond(status.httpStatus)


    }

}