package mimsoft.io.integrate.iiko

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToIIko() {
    route("/iiko") {
        get("/products") {
            val principal = call.principal<BasePrincipal>()!!
            val organizationId = call.parameters["organizationId"]
            call.respond(IIkoService.getProducts(organizationId, principal.merchantId) ?: HttpStatusCode.NoContent)
        }
        get("/orders") {
            call.respond(IIkoService.getOrders())
        }
        get("/branches") {
            val merchantId = call.parameters["merchantId"]?.toLongOrNull()
            call.respond(IIkoService.getBranches(merchantId) ?: HttpStatusCode.NoContent)
        }
        get("/branch") {
            val merchantId = call.parameters["branchId"]?.toLongOrNull()
            call.respond(IIkoService.getBranches(merchantId) ?: HttpStatusCode.NoContent)
        }
    }
}