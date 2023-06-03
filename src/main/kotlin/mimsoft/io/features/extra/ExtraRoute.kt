package mimsoft.io.features.extra

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.extra.ropository.ExtraRepository
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl

fun Route.routeToExtra() {

    val extraRepository: ExtraRepository = ExtraRepositoryImpl

    get("/extras") {
        val extras = extraRepository.getAll().map { ExtraMapper.toExtraDto(it) }
        if (extras.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, extras)
    }

    get("/extra/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val extra = ExtraMapper.toExtraDto(extraRepository.get(id))
        if (extra != null) {
            call.respond(HttpStatusCode.OK, extra)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/extra") {
        val extra = call.receive<ExtraDto>()
        val id = extraRepository.add(ExtraMapper.toExtraTable(extra))
        call.respond(HttpStatusCode.OK, ExtraId(id))
    }

    put("/extra") {
        val extra = call.receive<ExtraDto>()
        extraRepository.update(ExtraMapper.toExtraTable(extra))
        call.respond(HttpStatusCode.OK)
    }

    delete("/extra/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = extraRepository.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

data class ExtraId(
    val id: Long? = null
)