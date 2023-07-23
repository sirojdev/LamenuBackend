package mimsoft.io.features.story

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToStory() {
    val storyService = StoryService
    route("story") {
        post {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<StoryDto>()
            val response = storyService.add(dto.copy(merchantId = merchantId))
            call.respond(StoryId(response))
        }

        put {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<StoryDto>()
            val response = storyService.update(dto.copy(merchantId = merchantId))
            call.respond((response))
            return@put
        }

        get("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null)
                call.respond(HttpStatusCode.BadRequest)
            val response = storyService.getById(merchantId = merchantId, id = id)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        get {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val response = storyService.getAll(merchantId = merchantId)
            call.respond(response)
            return@get
        }


        delete("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null)
                call.respond(HttpStatusCode.BadRequest)
            val response = storyService.delete(merchantId = merchantId, id = id)
            if (!response) {
                call.respond(HttpStatusCode.NoContent)
                return@delete
            }
            call.respond(response)
        }

        put("update/priority") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            val priorityNumber = call.parameters["prNumber"]?.toLongOrNull()
            if (id == null || priorityNumber == null)
                call.respond(HttpStatusCode.BadRequest)
            val response = storyService.updatePriority(priorityNumber = priorityNumber, id = id, merchantId = merchantId)
            call.respond(response)
        }
    }
}


data class StoryId(
    val id: Long? = null
)
