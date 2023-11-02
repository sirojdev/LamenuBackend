package mimsoft.io.waiter.foods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category_group.CategoryGroupService
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.ResponseData

fun Route.routeToWaiterCategoryByGroup() {
  route("categoryByGroup") {
    get {
      val principal = getPrincipal()
      val categoryGroup =
        CategoryGroupService.getCategoryGroupWithBranchId(
          merchantId = principal?.merchantId,
          branchId = principal?.branchId
        )
      call.respond(ResponseData(data = categoryGroup))
    }

    get("{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      val principal = getPrincipal()
      val categoryList =
        CategoryGroupService.getCategoryGroupByIdInBranch(
          branchId = principal?.branchId,
          groupId = id
        )
      call.respond(ResponseData(data = categoryList))
    }
  }
}
