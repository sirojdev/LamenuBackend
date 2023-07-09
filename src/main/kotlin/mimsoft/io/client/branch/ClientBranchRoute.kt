package mimsoft.io.client.branch

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.repository.BranchService
import mimsoft.io.features.branch.repository.BranchServiceImpl

fun Route.routeToClientBranches() {
    val branchService: BranchService = BranchServiceImpl

    get("branches") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        if (merchantId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val branches = branchService.getAll(merchantId = merchantId)
        if (branches.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(branches)
    }
    get("branch") {
        val id = call.parameters["id"]?.toLongOrNull()
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val branchName = call.parameters["branch-name"]
        if (branchName != null && merchantId != null) {
            val branch = branchService.getByName(branchName, merchantId)
            if(branch!=null){
                call.respond(branch)
            }else{
                call.respond(HttpStatusCode.NoContent)
            }
        }
        if (merchantId == null || id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val branch = branchService.get(id = id, merchantId = merchantId)
        if (branch == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(branch)
    }
    get("nearest") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val latitude = call.parameters["lat"]?.toDoubleOrNull()
        val longitude = call.parameters["long"]?.toDoubleOrNull()
        if (latitude == null || longitude == null||merchantId==null) {
            call.respond(HttpStatusCode.BadRequest)
        }
        val branchDto = branchService.nearestBranch(latitude, longitude,merchantId)
        if (branchDto == null) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.OK, branchDto)
        }
    }

}