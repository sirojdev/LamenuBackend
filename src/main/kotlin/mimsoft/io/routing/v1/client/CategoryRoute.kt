package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.telegram_bot.Language

fun Route.routeToClientCategory() {

  val categoryRepository: CategoryRepository = CategoryRepositoryImpl

  get("categories") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val categories = categoryRepository.getAllByClient(merchantId = merchantId)
    if (categories.isEmpty()) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    } else call.respond(categories)
  }

  get("category/{id}") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
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
  get("categories/tg") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val name = call.parameters["name"]
    val lang = Language.valueOf(call.parameters["lang"] ?: "UZ")
    val categories =
      categoryRepository.getCategoryByCategoryGroupName(merchantId = merchantId, name, lang)
    if (categories.isNullOrEmpty()) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    } else call.respond(categories)
  }
}
