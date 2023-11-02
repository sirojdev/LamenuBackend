package mimsoft.io.features.manager_sys

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToManagers() {
  route("manager") {
    post {
      val model = call.receive<ManagerSysModel>()
      val response = ManagersService.addManager(model = model)
      call.respond(response)
    }

    get("{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val response = ManagersService.getManager(id = id)
      call.respond(response)
    }

    get {
      val response = ManagersService.getAllManager()
      call.respond(response)
    }

    delete("{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val response = ManagersService.deleteManager(id = id)
      call.respond(response)
    }

    put {
      val model = call.receive<ManagerSysModel>()
      val response = ManagersService.updateManager(model = model)
      call.respond(response)
    }
  }
}
