package mimsoft.io.courier.auth

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.merchant.repository.MerchantAuthImp
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal
import java.util.*

fun Route.routeToCourierAuth() {
    val courierService = CourierService
    val sessionRepo = SessionRepository
    route("courier") {
        route("device") {
            post {
                val device: DeviceModel = call.receive()
                val merchantId = call.parameters["appKey"]?.toLongOrNull()
                device.merchantId = merchantId
                println(Gson().toJson(device))
                if (device.brand == null || device.model == null || device.build == null || device.osVersion == null
                    || device.uuid.isNullOrBlank() || device.merchantId == null || device.firebaseToken == null) {
                    println(device.brand)
                    println(device.model)
                    println(device.build)
                    println(device.osVersion)
                    println(device.uuid)
                    println(device.merchantId)
                    call.respond(HttpStatusCode.BadRequest, "error input")
                } else {
                    val ip = call.request.host()
                    val result = DeviceController.auth(device.copy(ip = ip))
                    call.respond(result)
                }
            }
        }

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
                            token = JwtConfig.generateCourierToken(
                                staffId = authStaff.id,
                                merchantId = authStaff.merchantId,
                                uuid = uuid,
                            )
                        ) ?: HttpStatusCode.NoContent
                    )
                }
            }

        }

        authenticate("courier") {
            post("logout") {
                val merchant = call.principal<BasePrincipal>()
                CourierService.logout(merchant?.uuid)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}