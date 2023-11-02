package mimsoft.io.features.app

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToApp() {
  get("app") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val app = AppService.get(merchantId = merchantId) ?: AppDto()
    call.respond(app)
  }

  put("app") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val app = call.receive<AppDto>()
    AppService.add(app.copy(merchantId = merchantId))
    call.respond(HttpStatusCode.OK)
  }
}
