package mimsoft.io.features.news

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.news.repository.NewsRepository
import mimsoft.io.features.news.repository.NewsRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToNews() {
    val news: NewsRepository = NewsRepositoryImpl
    route("news") {
        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<NewsDto>()
            val response = news.add(dto.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, CategoryGroupId(response))
        }

        put {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<NewsDto>()
            val response = news.update(dto.copy(merchantId = merchantId))
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
            val response = news.getById(id = id, merchantId = merchantId)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        get {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val response = news.getAll(merchantId = merchantId)
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
            val response = news.delete(id = id, merchantId = merchantId)
            call.respond(response)
        }
    }
}

data class CategoryGroupId(val id: Long? = null)

