package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.announcement.repository.AnnounceRepositoryImpl


fun Route.routeToAnnouncement() {
    get("announcement") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val result = AnnounceRepositoryImpl.getAll(merchantId = merchantId)
        if(result.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, result)
        return@get
    }
}