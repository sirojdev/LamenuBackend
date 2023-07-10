package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl

fun Route.routeToClientCategory() {

    val categoryRepository: CategoryRepository = CategoryRepositoryImpl

    get("categories") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val categories = categoryRepository.getAll(merchantId = merchantId)
        if (categories.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(categories)
    }
}