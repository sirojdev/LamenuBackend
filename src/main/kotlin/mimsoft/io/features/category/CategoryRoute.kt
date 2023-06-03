package mimsoft.io.features.category

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl

fun Route.routeToCategory() {

    val categoryRepository: CategoryRepository = CategoryRepositoryImpl

    get("categories") {
        val categories = categoryRepository.getAll()
        if (categories.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(categories)
    }

    get("category/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val category = categoryRepository.get(id)
        if (category==null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(category)
    }

    post("category") {
        val category = call.receive<CategoryDto>()
        val status = categoryRepository.add(category)
        call.respond(HttpStatusCode.OK, status?:0)
    }

    put("category") {
        val category = call.receive<CategoryDto>()
        categoryRepository.update(category)
        call.respond(HttpStatusCode.OK)
    }

    delete("category/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        categoryRepository.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}