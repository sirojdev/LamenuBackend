package mimsoft.io.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.role.RoleService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.*

fun Route.routeToStaff() {

    val staffService = StaffService
    val mapper = StaffMapper
    val sessionRepo = SessionRepository
    val roleService = RoleService

    post("staff/auth") {
        val staff = call.receive<StaffDto>()
        val authStaff = staffService.auth(mapper.toTable(staff))
        if (authStaff.status != StatusCode.OK && authStaff.httpStatus != null)
            call.respond(authStaff.httpStatus, authStaff)
        else {

            val staffBody = authStaff.body as StaffTable

            val uuid = staffService.generateUuid(staffBody.id)
            val roles = roleService.getByStaff(staffBody.id)

            sessionRepo.auth(
                SessionTable(
                    uuid = uuid,
                    stuffId = staffBody.id,
                )
            )
            call.respond(
                staffBody.copy(
                    token = JwtConfig.generateAccessToken(
                        entityId = staffBody.id,
                        forUser = false,
                        uuid = uuid,
                        roles = roles
                    )
                )
            )
        }

    }

    authenticate("access") {

        get("staffs") {
            val staffs = staffService.getAll()
            call.respond(staffs.ifEmpty { HttpStatusCode.NoContent })
        }

        get("staff") {
            val id = call.parameters["id"]?.toLongOrNull()

            if(id==null){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val staff = staffService.get(id)
            if(staff == null){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(staff)
        }


        post("staff") {
            val principal = call.principal<LaPrincipal>()
            val staff = call.receive<StaffDto>()

            val statusTimestamp = timestampValidator(staff.birthDay)

            if (statusTimestamp.status != StatusCode.OK) {
                call.respond(statusTimestamp)
                return@post
            }

            val status = staffService.add(mapper.toTable(staff))

            if (status.httpStatus != null)
                call.respond(status.httpStatus, status)
            else call.respond(status)

        }

        put ("staff") {
            val principal = call.principal<LaPrincipal>()
            val staff = call.receive<StaffDto>()

            val statusTimestamp = timestampValidator(staff.birthDay)

            if (statusTimestamp.status != StatusCode.OK) {
                call.respond(statusTimestamp)
                return@put
            }

            val status = staffService.update(mapper.toTable(staff))

            if (status.httpStatus != null)
                call.respond(status.httpStatus, status)
            else call.respond(status)

        }
    }


}