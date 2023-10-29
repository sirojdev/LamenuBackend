package mimsoft.io.features.notification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Timestamp
import kotlin.math.min
import mimsoft.io.features.notification.repository.NotificationRepository
import mimsoft.io.features.notification.repository.NotificationRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToNotification() {

  val notification: NotificationRepository = NotificationRepositoryImpl

  route("notification") {
    get {
      val principal = call.principal<BasePrincipal>()
      val merchantId = principal?.merchantId
      val search = call.parameters["search"]
      val filters = call.parameters["filters"]
      val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val response =
        notification.getAll(
          merchantId = merchantId,
          search = search,
          filters = filters,
          limit = limit,
          offset = offset,
        )
      if (response?.data?.isNotEmpty() == true) {
        call.respond(response)
        return@get
      }
      call.respond(HttpStatusCode.NoContent)
    }

    post {
      val principal = call.principal<BasePrincipal>()
      val merchantId = principal?.merchantId
      val dto = call.receive<NotificationDto>()
      val response =
        notification.add(
          dto.copy(merchantId = merchantId, date = Timestamp(System.currentTimeMillis()))
        )
      call.respond(CategoryGroupId(response))
    }

    put {
      val principal = call.principal<BasePrincipal>()
      val merchantId = principal?.merchantId
      val dto = call.receive<NotificationDto>()
      val response = notification.update(dto.copy(merchantId = merchantId))
      if (response) call.respond(response)
      call.respond(HttpStatusCode.NoContent)
    }

    delete("{id}") {
      val principal = call.principal<BasePrincipal>()
      val merchantId = principal?.merchantId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val response = notification.delete(id = id, merchantId = merchantId)
      if (response) call.respond(response)
      call.respond(HttpStatusCode.NoContent)
    }

    get("{id}") {
      val principal = call.principal<BasePrincipal>()
      val merchantId = principal?.merchantId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val response = notification.getById(id = id, merchantId = merchantId)
      call.respond(response ?: HttpStatusCode.NoContent)
    }
  }
}

data class CategoryGroupId(val id: Long? = null)
