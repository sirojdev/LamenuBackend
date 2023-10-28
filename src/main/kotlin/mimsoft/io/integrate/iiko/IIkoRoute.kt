package mimsoft.io.integrate.iiko

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.iiko.model.IIkoErrorService
import mimsoft.io.integrate.iiko.model.Webhook
import mimsoft.io.integrate.uzum.UzumService
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.toJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToIIko() {
    val log: Logger = LoggerFactory.getLogger(IIkoService::class.java)
    route("/iiko") {
        post("webhook") {
            try {
                val body = call.receive<List<Webhook>>()
                log.info(Gson().toJson(body))
                for (w in body) {
                    if (w.eventInfo?.errorInfo != null) {
                        IIkoErrorService.saveError(webhook = w)
                    }
                }
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
            get("modifiers") {
                val principal = getPrincipal()
                val branchId = principal?.branchId
                val productId = call.parameters["id"]
                val merchantId = principal?.merchantId
                call.respond(IIkoService.getModifiersByProduct(branchId, merchantId, productId))
            }
            post("order") {
                call.respond(IIkoService.createOrder(1, 200))
            }
            get("payment") {
                val principal = getPrincipal()
                call.respond(IIkoService.getPayment(principal))
            }

        }
    }
}