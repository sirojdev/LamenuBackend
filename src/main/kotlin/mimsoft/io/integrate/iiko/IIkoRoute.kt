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
                val groupId = call.parameters["group_id"] ?: throw BadRequest("group id required")
                val categoryId = call.parameters["category_id"] ?: throw BadRequest("category id  required")
                call.respond(IIkoService.getProducts(principal, groupId, categoryId))
            }
            get("/organization") {
                val merchantId = getPrincipal()?.merchantId ?: throw BadRequest("merchant id required in principal")
                call.respond(IIkoService.getOrganization(merchantId))
            }
            get("/organization-full-info") {
                val merchantId = getPrincipal()?.merchantId
                val branchId = call.parameters["id"] ?: throw BadRequest("organization id required")
                call.respond(
                    IIkoService.getOrganizationFullInfo(iikoBranchId = branchId, merchantId = merchantId!!)
                )
            }
            get("groups") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getGroups(branchId, merchantId))
            }
            get("category") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getCategory(branchId, merchantId))
            }
            get("terminal-group") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getTerminalGroup(branchId, merchantId))
            }
            get("payment") {
                val principal = getPrincipal()
                call.respond(IIkoService.getPayment(principal))
            }
            post("webhook") {
                val params = call.parameters
                for (x in params.entries()) {
                    println(x)
                }
            }
        }
    }
}