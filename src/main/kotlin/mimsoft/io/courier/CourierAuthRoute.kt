package mimsoft.io.courier

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel
import java.util.*

fun Route.routeToCourierAuth() {
    val courierService = CourierService
    val sessionRepo = SessionRepository
    route("courier") {
        post("auth") {
            val authDto = call.receive<AuthDto>()
            val status = courierService.auth(authDto)
            if (status.httpStatus != ResponseModel.OK)
                call.respond(status.httpStatus, status)
            else {
                val authStaff = status.body as StaffDto?
                if(authStaff?.position!="courier"){
                    call.respond(ResponseModel(httpStatus = HttpStatusCode.NotFound))
                }
                val uuid = courierService.generateUuid(authStaff?.id)
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
                        )
                    ) ?: HttpStatusCode.NoContent
                )
            }
        }

    }

}