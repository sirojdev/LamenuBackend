package mimsoft.io.manager

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToManager() {

    val service = ManagerService

    get("/managers") {
        val managers = service.getAll()
        call.respond(managers.ifEmpty { HttpStatusCode.NotFound })
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

        call.respond(manager)
    }

    post("/manager") {
        val managerDto = call.receive<ManagerDto>()
        val status = service.add(managerDto)
        val manager = status.body as ManagerDto?
        call.respond(status.httpStatus, manager?.id?:0)

    }

    put("/manager") {
        val managerDto = call.receive<ManagerDto>()
        val result = service.update(managerDto)
        call.respond(result.httpStatus)
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