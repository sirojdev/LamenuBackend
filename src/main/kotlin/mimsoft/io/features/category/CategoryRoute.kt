package mimsoft.io.features.category

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToCategory() {

    val categoryRepository: CategoryRepository = CategoryRepositoryImpl

    get("categories") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val categories = categoryRepository.getAllByMerchant(merchantId = merchantId)
        if (categories.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(categories)
    }

    get("category/{id}") {
        val pr = call.principal<MerchantPrincipal>()
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
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val category = call.receive<CategoryDto>()
        val status = categoryRepository.add(category.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK, status?:0)
    }

    put("category") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val category = call.receive<CategoryDto>()
        categoryRepository.update(category.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("category/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        categoryRepository.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}