package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.utils.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToStaff() {

    route("staff") {

        get {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val staffs = StaffService.getAll(merchantId = merchantId)
            call.respond(staffs.ifEmpty { HttpStatusCode.NoContent })
        }

        get("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val staff = StaffService.get(id = id, merchantId = merchantId)
            if (staff == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(staff)
        }


        post {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val staff = call.receive<StaffDto>()
            val statusTimestamp = timestampValidator(staff.birthDay)
            if (statusTimestamp.httpStatus != OK) {
                call.respond(statusTimestamp)
                return@post
            }
            val status = StaffService.add(staff.copy(merchantId = merchantId))
            call.respond(status.httpStatus, status)
        }

        put {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val staff = call.receive<StaffDto>()
            val statusTimestamp = timestampValidator(staff.birthDay)
            if (statusTimestamp.httpStatus != OK) {
                call.respond(statusTimestamp)
                return@put
            }
            val status = StaffService.update(staff.copy(merchantId = merchantId))
            call.respond(status.httpStatus, status)
        }

        delete("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId

            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val staff = StaffService.delete(id = id, merchantId = merchantId)
            call.respond(staff)
        }

    }


}