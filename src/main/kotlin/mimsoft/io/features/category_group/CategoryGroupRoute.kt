package mimsoft.io.features.category_group

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCategoryGroup() {
    val categoryGroupService = CategoryGroupService
    route("category/group") {
        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<CategoryGroupDto>()
            val response = categoryGroupService.add(dto.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, CategoryGroupId(response))
        }

        put {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<CategoryGroupDto>()
            val response = categoryGroupService.update(dto.copy(merchantId = merchantId))
            call.respond(response)
        }

        get("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = categoryGroupService.getById(id = id, merchantId = merchantId)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }
        get {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val response = categoryGroupService.getAll(merchantId = merchantId)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        delete("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = categoryGroupService.delete(id = id, merchantId = merchantId)
            call.respond(response)
        }
    }
}

data class CategoryGroupId(val id: Long? = null)

