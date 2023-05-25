package mimsoft.io.entities.menu

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.menu.repository.MenuRepository
import mimsoft.io.entities.menu.repository.MenuRepositoryImpl
import mimsoft.io.utils.Role
import mimsoft.io.utils.authorize


fun Route.routeToMenu() {

    authenticate("access") {

        authorize(Role.USER){

            val menuRepository: MenuRepository = MenuRepositoryImpl
            get("/menus") {
                val menus = menuRepository.getAll().map { MenuMapper.toMenuDto(it) }
                if (menus.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent)
                    return@get
                }
                call.respond(HttpStatusCode.OK, menus)
            }

            get("/menu/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val menu = MenuMapper.toMenuDto(menuRepository.get(id))
                if (menu != null) {
                    call.respond(HttpStatusCode.OK, menu)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            post("/menu") {
                val menu = call.receive<MenuDto>()
                val id = menuRepository.add(MenuMapper.toMenuTable(menu))
                call.respond(HttpStatusCode.OK, MenuId(id))
            }

            put("/menu") {
                val menu = call.receive<MenuDto>()
                menuRepository.update(MenuMapper.toMenuTable(menu))
                call.respond(HttpStatusCode.OK)
            }

            delete("/menu/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id != null) {
                    val deleted = menuRepository.delete(id)
                    if (deleted) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                } else
                    call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

data class MenuId(
    val id: Long? = null
)