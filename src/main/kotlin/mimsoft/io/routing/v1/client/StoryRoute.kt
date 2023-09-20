package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.story.StoryService
import mimsoft.io.features.story_info.StoryInfoService

fun Route.routeToClientStory() {
    val storyService = StoryService
    val storyInfoService = StoryInfoService
    get("stories") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val response = storyService.getAll(merchantId = merchantId)
        call.respond(response)
        return@get
    }

    get("story/info") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        if (merchantId == null) {
            call.respond(HttpStatusCode.BadRequest)
        }
        val response = storyInfoService.getAll(merchantId = merchantId)
        call.respond(response)
        return@get
    }
}