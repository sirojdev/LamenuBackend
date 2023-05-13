package mimsoft.io.entities.option

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.option.repository.OptionRepository
import mimsoft.io.entities.option.repository.OptionRepositoryImpl


fun Route.routeToOption() {

    val optionRepository: OptionRepository = OptionRepositoryImpl

    get("/options") {
        val options = optionRepository.getAll().map { OptionMapper.toOptionDto(it) }
        if (options.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, options)
    }

    get("/option/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val option = OptionMapper.toOptionDto(optionRepository.get(id))
        if (option != null) {
            call.respond(HttpStatusCode.OK, option)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/option") {
        val option = call.receive<OptionDto>()
        val id = optionRepository.add(OptionMapper.toOptionTable(option))
        call.respond(HttpStatusCode.OK, OptionId(id))
    }

    put("/option") {
        val option = call.receive<OptionDto>()
        optionRepository.update(OptionMapper.toOptionTable(option))
        call.respond(HttpStatusCode.OK)
    }

    delete("/option/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = optionRepository.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else
            call.respond(HttpStatusCode.BadRequest)
    }
}
data class OptionId(
    val id: Long? = null
)