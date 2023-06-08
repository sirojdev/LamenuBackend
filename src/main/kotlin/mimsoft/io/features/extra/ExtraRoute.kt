package mimsoft.io.features.extra

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.extra.ropository.ExtraRepository
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal
import kotlin.collections.map

fun Route.routeToExtra() {

    val extraRepository: ExtraRepository = ExtraRepositoryImpl
    val mapper = ExtraMapper
    get("/extras") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val extras = extraRepository.getAll(merchantId = merchantId).map { mapper.toExtraDto(it) }
        if (extras.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, extras)
    }

    get("/extra/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val extra = mapper.toExtraDto(extraRepository.get(id = id, merchantId=merchantId))
        if (extra != null) {
            call.respond(HttpStatusCode.OK, extra)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/extra") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val extra = call.receive<ExtraDto>()
        println(extra)
        val id = extraRepository.add(mapper.toExtraTable(extra.copy(merchantId = merchantId)))
        call.respond(HttpStatusCode.OK, ExtraId(id))
    }

    put("/extra") {
        val extra = call.receive<ExtraDto>()
        println("Extra-------------------------------->>>$extra")
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        extraRepository.update(extra.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("/extra/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = extraRepository.delete(id, merchantId)
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

data class ExtraId(
    val id: Long? = null
)