package mimsoft.io.features.story_info

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToStoryInfo() {
    val storyInfoService = StoryInfoService
    route("story/info") {

        get("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null)
                call.respond(HttpStatusCode.BadRequest)
            val response = storyInfoService.getById(merchantId = merchantId, id = id)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        get {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val response = storyInfoService.getAll(merchantId = merchantId)
            call.respond(response)
            return@get
        }
    }
}
