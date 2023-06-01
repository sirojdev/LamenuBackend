package mimsoft.io.entities.poster

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

    post ("poster"){
        val merchantId = 1L
        val table = call.receive<PosterDto>()
        PosterService.add(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }
}