package mimsoft.io.features.notification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.notification.repository.NotificationRepository
import mimsoft.io.features.notification.repository.NotificationRepositoryImpl
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.principal.MerchantPrincipal
import java.sql.Timestamp

fun Route.routeToNotification() {
    val notification: NotificationRepository = NotificationRepositoryImpl
    route("notification") {

        post {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<NotificationDto>()
            val response = notification.add(dto.copy(merchantId = merchantId, date = Timestamp(System.currentTimeMillis())))
            call.respond(HttpStatusCode.OK, CategoryGroupId(response))
        }

        put {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val dto = call.receive<NotificationDto>()
            val response = notification.update(dto.copy(merchantId = merchantId))
            call.respond(response)
        }

        get("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = notification.getById(id = id, merchantId = merchantId)
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        get {
            val merchantPrincipal = call.principal<MerchantPrincipal>()
            val staffPrincipal = call.principal<StaffPrincipal>()
            val limit = call.parameters["limit"]?.toIntOrNull()
            val offset = call.parameters["offset"]?.toIntOrNull()
            val merchantId = merchantPrincipal?.merchantId?: staffPrincipal?.merchantId
            val response = notification.getAll(merchantId = merchantId, limit = limit, offset = offset)
            call.respond(response?: HttpStatusCode.NoContent)
        }

        delete("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = notification.delete(id = id, merchantId = merchantId)
            call.respond(response)
        }
    }
}

data class CategoryGroupId(val id: Long? = null)

