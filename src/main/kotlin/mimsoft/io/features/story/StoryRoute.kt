package mimsoft.io.features.story

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToStory() {
    val storyService = StoryService
    route("story") {

        get("{id}") {
            val pr = call.principal<BasePrincipal>()
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
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val response = storyService.getAll(merchantId = merchantId)
            call.respond(response)
            return@get
        }
    }
}