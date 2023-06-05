package mimsoft.io.features.poster

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToPoster(){
    get("poster"){
        val merchantId = 1L
        val poster = PosterService.get(merchantId=merchantId)?: PosterDto()
        call.respond(poster)
    }

    put ("poster"){
        val merchantId = 1L
        val poster = call.receive<PosterDto>()
        PosterService.add(poster.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }
}