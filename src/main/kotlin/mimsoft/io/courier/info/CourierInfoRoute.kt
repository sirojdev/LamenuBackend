package mimsoft.io.courier.info

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.routing.v1.client.generateSessionUUID
import mimsoft.io.rsa.Generator
import mimsoft.io.rsa.GeneratorModel
import mimsoft.io.rsa.Status
import mimsoft.io.services.sms.SmsSenderService
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCouriersInfo() {
    val courierService = CourierService
    get("profile") {
        val principal = call.principal<BasePrincipal>()
        val courierId = principal?.staffId
        val dto = courierService.getById(courierId)
        if (dto == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(dto)
        }
    }
    patch("update") {
        val principal = call.principal<BasePrincipal>()
        val dto = call.receive<StaffDto>()
        call.respond(courierService.updateCourierInfo(dto.copy(id = principal?.staffId)))
    }

    post("firebase") {
        val principal = call.principal<BasePrincipal>()
        val uuid = principal?.uuid
        val firebase = call.parameters["firebase"]
        call.respond(DeviceController.editFirebase(sessionUUID  = uuid,token =firebase ))
    }

    /**
     * COURIER PHONE NI ALMASHTIRISH KEYINCHALIK QILINADI
     * */
//    get("phone") {
//        val principal = call.principal<BasePrincipal>()
//        val staffId = principal?.staffId
//        val uuid = principal?.uuid
//        val merchantId = principal?.merchantId
//        val phone = call.parameters["newPhone"]
//        call.respond(CourierService.updatePhone(staffId, phone, principal?.merchantId))
//        val gn = Generator.generate(true)
//        if (phone != null) {
//            SmsSenderService.send(merchantId = principal?.merchantId, phone, "code ${gn.code}")
//        } else {
//            call.respond(ResponseModel(body = " phone is null", HttpStatusCode.BadRequest))
//        }
//        val deviceM = DeviceController.getWithUUid(uuid)
//
//        DeviceController.updateCode(
//            DeviceModel(
//                id = deviceM?.id,
//                action = "verify",
//                merchantId = merchantId,
//                code = gn.code.toString(),
//                phone = phone
//            )
//        )
//
//        call.respond(
//            DeviceModel(
//                id = deviceM?.id,
//                token = JwtConfig.generateDeviceToken(
//                    merchantId = merchantId,
//                    uuid = uuid,
//                    hash = gn.hash,
//                    phone = phone
//                )
//            )
//        )
//    }
//    post("verify") {
//        val principal = call.principal<DevicePrincipal>()
//        val device = call.receive<DeviceModel>()
//
//        val checkCode = Generator.checkValidate(
//            GeneratorModel(
//                hash = principal?.hash,
//                code = device.code?.toLongOrNull()
//            )
//        )
//
//        if (checkCode != Status.ACCEPTED) {
//            when (checkCode) {
//                Status.INVALID_CODE -> call.respond(HttpStatusCode.NotAcceptable)
//                Status.GONE_CODE -> call.respond(HttpStatusCode.Gone)
//                else -> call.respond(HttpStatusCode.BadRequest)
//            }
//        } else {
//            val oldCode = DeviceController.updateCode(
//                DeviceModel(
//                    id = principal?.id,
//                    code = null,
//                    phone = null,
//                    merchantId = principal?.merchantId,
//                    action = "login"
//                )
//            )
//            if (oldCode != device.code?.toLongOrNull()) {
//                call.respond(HttpStatusCode.NotAcceptable)
//            } else {
//                val user = userRepository.get(phone = principal?.phone, merchantId = principal?.merchantId)
//
//                if (user != null) {
//                    val sessionUuid = generateSessionUUID(uuid = principal?.uuid, id = principal?.id)
//                    sessionRepository.auth(
//                        SessionTable(
//                            uuid = sessionUuid,
//                            userId = user.id,
//                            deviceId = principal?.id,
//                            merchantId = principal?.merchantId
//                        )
//                    )
//                    device.token = JwtConfig.generateUserToken(
//                        merchantId = principal?.merchantId,
//                        uuid = sessionUuid
//                    )
//                    device.action = "sign-in"
//                    call.respond(device)
//                }
//                //                else {
////                    device.token = JwtConfig.generateModifyToken(
////                        sessionUuid = principal?.uuid,
////                    )
////                    device.action = "sign-up"
////                    call.respond(device)
////                }
//            }
//        }
//    }

}