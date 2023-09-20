package mimsoft.io.features.stoplist

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToStopList() {
    route("stoplist") {
        get("increment/{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val stopList = StopListService.decrementCount(id = id, merchantId = merchantId)
            call.respond(stopList)
        }

        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val stopList = call.receive<StopListDto>()
            StopListService.add(stopList.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK)
        }


        put {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val stopList = call.receive<StopListDto>()
            StopListService.update(stopList.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK)
        }

    }
}

