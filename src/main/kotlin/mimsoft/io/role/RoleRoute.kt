package mimsoft.io.role

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mimsoft.io.utils.Role
import mimsoft.io.utils.authorize

fun Route.routeToRole() {

    val roleService = RoleService

    /*authorize(Role.MANAGER_STAFFS){
        post("role") {
            val role = call.receive<RoleDto>()

            roleService.add(role)
        }
    }*/
}