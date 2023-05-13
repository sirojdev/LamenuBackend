package mimsoft.io.entities.category

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.category.repository.CategoryRepository
import mimsoft.io.entities.category.repository.CategoryRepositoryImpl

fun Route.routeToCategory() {

    val categoryRepository: CategoryRepository = CategoryRepositoryImpl

    get("categories") {
        val categories = categoryRepository.getAll().map { CategoryMapper.toCategoryDto(it) }
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
        val category = CategoryMapper.toCategoryDto(categoryRepository.get(id))
        if (category==null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(category)
    }

    post("category") {
        val category = call.receive<CategoryDto>()
        categoryRepository.add(CategoryMapper.toCategoryTable(category))
        call.respond(HttpStatusCode.OK)
    }

    put("category") {
        val category = call.receive<CategoryDto>()
        categoryRepository.update(CategoryMapper.toCategoryTable(category))
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