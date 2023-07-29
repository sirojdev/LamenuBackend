package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel

fun Route.routeToStaffAuth(){

    val staffService = StaffService
    val sessionRepo = SessionRepository

    post("auth") {
        val staff = call.receive<StaffDto>()
        val status = staffService.auth(staff)

        if (status.httpStatus != ResponseModel.OK)
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

}