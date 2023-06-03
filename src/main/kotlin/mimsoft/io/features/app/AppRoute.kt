package mimsoft.io.features.app

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.poster.PosterDto
import mimsoft.io.features.poster.PosterService

fun Route.routeToApp(){
    get("app"){
        val merchantId = 1L
        val app = AppService.get(merchantId=merchantId)?: AppDto()
        call.respond(app)
    }

    post ("app"){
        val merchantId = 1L
        val app = call.receive<AppDto>()
        AppService.add(app.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }
}