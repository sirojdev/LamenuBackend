package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToStaff() {

  route("staff") {
    get {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val staffs = StaffService.getAll(merchantId = merchantId, branchId = branchId)
      call.respond(staffs.ifEmpty { HttpStatusCode.NoContent })
    }

    get("{id}") {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val staff = StaffService.getById(id = id, merchantId = merchantId, branchId = branchId)
      if (staff == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(staff)
    }

    post {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val staff = call.receive<StaffDto>()
      val statusTimestamp = timestampValidator(staff.birthDay)
      if (statusTimestamp.httpStatus != ResponseModel.OK) {
        call.respond(statusTimestamp)
        return@post
      }
      val status = StaffService.add(staff.copy(merchantId = merchantId, branchId = branchId))
      call.respond(status.httpStatus, status)
    }

    put {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val staff = call.receive<StaffDto>()
      val statusTimestamp = timestampValidator(staff.birthDay)
      if (statusTimestamp.httpStatus != ResponseModel.OK) {
        call.respond(statusTimestamp)
        return@put
      }
      val status = StaffService.update(staff.copy(merchantId = merchantId, branchId = branchId))
      if (!status) {
        call.respond(HttpStatusCode.NoContent)
      }
      call.respond(HttpStatusCode.OK)
    }

    delete("{id}") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId

      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val staff = StaffService.delete(id = id, merchantId = merchantId, branchId = branchId)
      call.respond(staff)
    }
  }
}