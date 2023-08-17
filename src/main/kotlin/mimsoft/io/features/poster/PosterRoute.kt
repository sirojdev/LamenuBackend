package mimsoft.io.features.poster

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToPoster() {
    get("poster") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val poster = PosterService.get(merchantId = merchantId) ?: PosterDto()
        call.respond(poster)
    }

    put("poster") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val poster = call.receive<PosterDto>()
        PosterService.add(poster.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }
}