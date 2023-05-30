package mimsoft.io.entities.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.utils.*

fun Route.routeToStaff() {

    route("staffs") {

        get {
            val staffs = StaffService.getAll()
            call.respond(staffs.ifEmpty { HttpStatusCode.NoContent })
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val staff = StaffService.get(id = id)
            if (staff == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(staff)
        }


        post {
            val principal = call.principal<LaPrincipal>()
            val staff = call.receive<StaffDto>()
            val statusTimestamp = timestampValidator(staff.birthDay)
            if (statusTimestamp.httpStatus != OK) {
                call.respond(statusTimestamp)
                return@post
            }
            val status = StaffService.add(staff)
            call.respond(status.httpStatus, status)
        }

        put {
            val principal = call.principal<LaPrincipal>()
            val staff = call.receive<StaffDto>()
            val statusTimestamp = timestampValidator(staff.birthDay)
            if (statusTimestamp.httpStatus != OK) {
                call.respond(statusTimestamp)
                return@put
            }
            val status = StaffService.update(staff)
            call.respond(status.httpStatus, status)
        }

        delete ("{id}"){
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val staff = StaffService.delete(id = id)
            call.respond(staff)
        }

    }


}