package mimsoft.io.features.category

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCategory() {

    val categoryRepository: CategoryRepository = CategoryRepositoryImpl

    get("categories") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val categories = categoryRepository.getAll(merchantId = merchantId)
        if (categories.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(categories)
    }

    get("category/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val category = categoryRepository.get(id = id, merchantId= merchantId)
        if (category==null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(category)
    }

    post("category") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val category = call.receive<CategoryDto>()
        val response = categoryRepository.add(category.copy(merchantId = merchantId))
        if(response != null){
            call.respond(response)
            return@post
        }
        else call.respond(CategoryId(response))
    }

    put("category") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val category = call.receive<CategoryDto>()
        val response = categoryRepository.update(category.copy(merchantId = merchantId))
        call.respond(response)
    }

    delete("category/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val response = categoryRepository.delete(id = id, merchantId = merchantId)
        call.respond(response)
    }
}

data class CategoryId(
    val id: Long? = null
)