package mimsoft.io.features.label

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.label.repository.LabelRepository
import mimsoft.io.features.label.repository.LabelRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal
import kotlin.collections.map

fun Route.routeToLabel() {

    val labelRepository: LabelRepository = LabelRepositoryImpl
    get("/labels") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val labels = labelRepository.getAll(merchantId = merchantId).map { LabelMapper.toLabelDto(it) }
        if (labels.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond (HttpStatusCode.OK, labels)
    }

    get("/label/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val label = LabelMapper.toLabelDto(labelRepository.get(id = id, merchantId = merchantId))
        if (label != null) {
            call.respond(HttpStatusCode.OK, label)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/label") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val label = call.receive<LabelDto>()
        val id = labelRepository.add(LabelMapper.toLabelTable(label.copy(merchantId = merchantId)))
        call.respond(HttpStatusCode.OK, LabelId(id))
    }

    put("/label") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val label = call.receive<LabelDto>()
        labelRepository.update(label.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("/label/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = labelRepository.delete(id = id, merchantId = merchantId)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

data class LabelId(
    val id: Long? = null
)