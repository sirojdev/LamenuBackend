package mimsoft.io.features.favourite

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel

fun Route.routeToFavourites() {
    val favouriteService = FavouriteService
    route("favourite"){
        post {
            val pr = call.principal<UserPrincipal>()
            val merchantId = pr?.merchantId
            val clientId = pr?.id
            val favouriteDto = call.receive<FavouriteDto>()
            favouriteService.add(favouriteDto.copy(merchantId=merchantId, clientId = clientId))
            call.respond(HttpStatusCode.OK)
        }

        put{
            val pr = call.principal<UserPrincipal>()
            val merchantId = pr?.merchantId
            val favouriteDto = call.receive<FavouriteDto>()
            favouriteService.update(favouriteDto.copy(merchantId=merchantId))
            call.respond(HttpStatusCode.OK)
        }

        get{
            val pr = call.principal<UserPrincipal>()
            val clientId = pr?.id
            val merchantId = pr?.merchantId
            val favourites = favouriteService.getAll(clientId=clientId, merchantId=merchantId)
            ResponseModel(favourites, OK)
            return@get
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val result = favouriteService.delete(id = id)
            ResponseModel(result, HttpStatusCode.OK)
        }

        delete {
            val clientId = 1L
            val result = favouriteService.deleteAll(clientId = clientId)
            ResponseModel(result, HttpStatusCode.OK)
        }
    }
}