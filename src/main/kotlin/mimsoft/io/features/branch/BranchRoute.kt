package mimsoft.io.features.branch

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.repository.BranchService
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.integrate.jowi.JowiService
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToBranch() {

    val branchService: BranchService = BranchServiceImpl

    get("branches") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branches = branchService.getAll(merchantId = merchantId)
        if (branches.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(branches)
    }

    get("branch/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
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

    post("branch") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branch = call.receive<BranchDto>()
        val id = branchService.add(branch.copy(merchantId = merchantId))
        if (branch.id!=null&&branch.merchantId!=null&&branch.jowiId!=null){
            JowiService.joinRestaurant(branchId =branch.id, restaurantId = branch.jowiId, merchantId=branch.merchantId)
        }
        call.respond(HttpStatusCode.OK, BranchId(id))
    }

    put("branch") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branch = call.receive<BranchDto>()
        branchService.update(branch.copy(merchantId = merchantId))
        if (branch.id!=null&&branch.merchantId!=null&&branch.jowiId!=null){
            JowiService.joinRestaurant(branchId =branch.id, restaurantId = branch.jowiId, merchantId=branch.merchantId)
        }
        call.respond(HttpStatusCode.OK)
    }

    delete("branch/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        branchService.delete(id)
        call.respond(HttpStatusCode.OK)
    }

}

data class BranchId(
    val id: Long? = null
)