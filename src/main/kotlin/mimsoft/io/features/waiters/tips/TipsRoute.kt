package mimsoft.io.features.waiters.tips

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToWaiterTips() {
  route("tips") {
    post {
      val model = call.receive<TipsModel>()
      val response = TipsService.add(model = model)
      if (response) {
        call.respond(HttpStatusCode.OK)
        return@post
      } else call.respond(HttpStatusCode.NotFound)
    }
  }
}
