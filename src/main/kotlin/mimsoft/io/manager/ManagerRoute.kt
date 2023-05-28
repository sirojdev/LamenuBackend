package mimsoft.io.manager

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.StatusCode

fun Route.routeToManager() {

    val service = ManagerService
    val mapper = ManagerMapper

    get("/managers") {
        val managers = service.getAll()
        call.respond(managers.map { mapper.toDto(it) }
            .ifEmpty { HttpStatusCode.NotFound })
    }

    get("/manager/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val manager = service.get(id)
        if (manager == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        val managerDto = mapper.toDto(manager)
        if (managerDto == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(managerDto)
    }

    post("/manager") {
        val managerDto = call.receive<ManagerDto>()
        val managerTable = mapper.toTable(managerDto)
        if (managerTable == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val status = service.add(managerTable)
        call.respond(status)

    }

    put("/manager") {
        val managerDto = call.receive<ManagerDto>()
        val managerTable = mapper.toTable(managerDto)
        if (managerTable == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }
        val result = service.update(managerTable)
        call.respond(result)
    }
    delete {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val result = service.delete(id)
        if (!result) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        call.respond(HttpStatusCode.OK)
    }
}