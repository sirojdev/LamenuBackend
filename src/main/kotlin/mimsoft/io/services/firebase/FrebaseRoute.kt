package mimsoft.io.services.firebase

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.notification.NotificationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToFirebase() {

  val log: Logger = LoggerFactory.getLogger("FirebaseRoute")
  post("/firebase/send") {
    val receive: NotificationDto = call.receive()
    log.info("FirebaseRoute: $receive")
    FirebaseService.sendNotification(receive)
    call.respond(HttpStatusCode.OK)
  }
}
