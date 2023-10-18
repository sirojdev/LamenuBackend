package mimsoft.io.integrate.iiko

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToIIko() {
    route("/iiko") {
        authenticate("branch") {
            get("/products") {
                val principal = getPrincipal()
                val groupId = call.parameters["group_id"]?:throw BadRequest("group id required")
                val categoryId = call.parameters["category_id"]?:throw BadRequest("category id  required")
                call.respond(IIkoService.getProducts(principal,groupId,categoryId))
            }
            get("/branches") {
                val merchantId = getPrincipal()?.merchantId?:throw BadRequest("merchant id required in principal")
                call.respond(IIkoService.getBranches(merchantId) ?: HttpStatusCode.NoContent)
            }
            get("/branch") {
                val merchantId = getPrincipal()?.merchantId
                val branchId = call.parameters["branchId"] ?: throw BadRequest("branchId required")
                call.respond(
                    IIkoService.getBranch(iikoBranchId = branchId, merchantId = 1)
                )
            }
            get("groups") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getGroups(branchId, merchantId) ?: HttpStatusCode.NoContent)
            }
            get("category") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getCategory(branchId, merchantId) ?: HttpStatusCode.NoContent)
            }
            get("terminal-group") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getCategory(branchId, merchantId) ?: HttpStatusCode.NoContent)
            }
        }
    }
}