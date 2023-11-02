package mimsoft.io.features.income

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.min
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToIncome() {
  route("income") {
    get {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val search = call.parameters["search"]
      val filter = call.parameters["filter"]
      val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val response =
        IncomeService.getAll(
          merchantId = merchantId,
          branchId = branchId,
          search = search,
          filter = filter,
          limit = limit,
          offset = offset
        )
      call.respond(response)
    }
  }
}
