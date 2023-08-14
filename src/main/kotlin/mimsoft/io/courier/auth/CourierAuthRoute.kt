package mimsoft.io.courier.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DevicePrincipal
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

        authenticate("device") {
            post("auth") {

                val device = call.principal<DevicePrincipal>()
                val courier = call.receive<StaffDto>()
                val status = courierService.auth(courier.copy(merchantId = device?.merchantId))

                if (status.httpStatus != ResponseModel.OK)
                    call.respond(status.httpStatus, status)
                else {
                    val authStaff = status.body as StaffDto?
                    if (authStaff?.position != "courier") {
                        call.respond(ResponseModel(httpStatus = HttpStatusCode.NotFound))
                    }
                    val uuid = courierService.generateUuid(authStaff?.id)
                    sessionRepo.auth(
                        SessionTable(
                            uuid = uuid,
                            stuffId = authStaff?.id,
                            merchantId = authStaff?.merchantId
                        )
                    )

                    call.respond(
                        authStaff?.copy(
                            token = JwtConfig.generateStaffToken(
                                staffId = authStaff.id,
                                merchantId = authStaff.merchantId,
                                uuid = uuid,
                            )
                        ) ?: HttpStatusCode.NoContent
                    )
                }
            }

        }
    }

}