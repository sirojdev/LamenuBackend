package mimsoft.io.waiter

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category_group.CategoryGroupService
import mimsoft.io.utils.plugins.getPrincipal


fun Route.routeToWaiterCategoryByGroup() {
    route("categoryByGroup") {
        get {
            val principal = getPrincipal()
            val appKey = call.parameters["appKey"]?.toLongOrNull()
            val response =
                CategoryGroupService.getCategoryGroupWithBranchId(merchantId = appKey, branchId = principal?.branchId)
            if (response.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
            return@get
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            val merchantId = call.parameters["appKey"]?.toLongOrNull()
            val response = CategoryGroupService.getCategoryGroupById(merchantId = merchantId, id = id)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
            return@get
        }
    }
}