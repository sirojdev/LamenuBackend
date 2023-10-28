package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.appKey.MerchantAppKeyRepository
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
    get("branch/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val branchName = call.parameters["branch-name"]
        if (branchName != null && merchantId != null) {
            val branch = branchService.getByName(branchName, merchantId)
            if (branch != null) {
                call.respond(branch)
                return@get
            } else {
                call.respond(HttpStatusCode.NoContent)
                return@get
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
    get("branch/byName") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val branchName = call.parameters["branch-name"]
        if (branchName != null && merchantId != null) {
            val branch = branchService.getByName(branchName, merchantId)
            if (branch != null) {
                call.respond(branch)
                return@get
            } else {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
        }
    }
    get("branch/nearest") {
        val appKey = call.parameters["appKey"]?.toLongOrNull()
        val merchantId = MerchantAppKeyRepository.getByAppId(appKey)?.merchantId
        val latitude = call.parameters["lat"]?.toDoubleOrNull()
        val longitude = call.parameters["long"]?.toDoubleOrNull()
        if (latitude == null || longitude == null || merchantId == null) {
            call.respond(HttpStatusCode.BadRequest)
        }else{
            val branchDto = branchService.nearestBranch(latitude, longitude, merchantId)
            if (branchDto == null) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, branchDto)
            }
        }

    }

}