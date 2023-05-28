package mimsoft.io.entities.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.role.RoleService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.StatusCode

fun Route.routeToStaffAuth(){

    val staffService = StaffService
    val mapper = StaffMapper
    val sessionRepo = SessionRepository
    val roleService = RoleService

    post("staff/auth") {
        val staff = call.receive<StaffDto>()
        val authStaff = StaffService.auth(StaffMapper.toTable(staff))
        if (authStaff.status != StatusCode.OK && authStaff.httpStatus != null)
            call.respond(authStaff.httpStatus, authStaff)
        else {

            val staffBody = StaffMapper.toDto(authStaff.body as StaffTable)

            val uuid = StaffService.generateUuid(staffBody?.id)

            sessionRepo.auth(
                SessionTable(
                    uuid = uuid,
                    stuffId = staffBody?.id,
                )
            )

            call.respond(
                staffBody?.copy(
                    token = JwtConfig.generateAccessToken(
                        entityId = staffBody.id,
                        forUser = false,
                        uuid = uuid,
                    )
                )?: HttpStatusCode.NoContent
            )
        }

    }

}