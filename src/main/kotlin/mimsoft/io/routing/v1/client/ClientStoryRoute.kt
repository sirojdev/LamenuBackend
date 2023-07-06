package mimsoft.io.routing.v1.client

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.story.StoryService

fun Route.routeToClientStory(){
    val storyService = StoryService
    route("story"){
        get{
            val pr = call.principal<UserPrincipal>()
            val merchantId = pr?.merchantId
            println(merchantId)
            val response = storyService.getAll(merchantId = merchantId)
            call.respond(response)
            return@get
        }
    }
}