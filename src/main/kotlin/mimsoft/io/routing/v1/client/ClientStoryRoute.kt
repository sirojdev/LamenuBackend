package mimsoft.io.routing.v1.client

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.story.StoryService

fun Route.routeToClientStory(){
    val storyService = StoryService
    route("stories"){
        get{
            val merchantId = call.parameters["appKey"]?.toLongOrNull()
            val response = storyService.getAll(merchantId = merchantId)
            call.respond(response)
            return@get
        }
    }
}