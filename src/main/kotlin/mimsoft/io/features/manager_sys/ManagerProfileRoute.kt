package mimsoft.io.features.manager_sys

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToManagerProfile() {
  get {
    val pr = getPrincipal()
    val id = pr?.id
    val response = ManagersService.getManager(id = id)
    call.respond(response)
  }

  put {
    val pr = getPrincipal()
    val id = pr?.id
    val model = call.receive<ManagerSysModel>()
    val response = ManagersService.updateManager(model = model.copy(id = id))
    call.respond(response)
  }
}
