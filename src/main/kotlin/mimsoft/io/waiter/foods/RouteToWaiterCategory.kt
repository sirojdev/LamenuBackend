package mimsoft.io.waiter.foods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.ResponseData

fun Route.routeToWaiterCategory() {
  val categoryRepository: CategoryRepository = CategoryRepositoryImpl
  get("categories") {
    val merchantId = getPrincipal()?.merchantId
    val categories = categoryRepository.getAllByClient(merchantId = merchantId)
    if (categories.isEmpty()) {
      call.respond(ResponseData(statusCode = HttpStatusCode.NoContent, data = categories))
      return@get
    } else call.respond(ResponseData(statusCode = HttpStatusCode.OK, data = categories))
  }

  get("category/{id}") {
    val merchantId = getPrincipal()?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val category = categoryRepository.getCategoryForClientById(id = id, merchantId = merchantId)
    if (category == null) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    }
    call.respond(category)
    return@get
  }
}
