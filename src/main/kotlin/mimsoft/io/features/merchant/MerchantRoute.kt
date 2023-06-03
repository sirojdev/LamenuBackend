package mimsoft.io.features.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.repository.MerchantInterface
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp

fun Route.merchantRoute() {

    val merchantRepository: MerchantInterface = MerchantRepositoryImp
    route("merchant") {

        get("info") {
            val sub = call.parameters["sub"]
            if (sub == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val merchant = merchantRepository.getInfo(sub)
            if (merchant != null) {
                call.respond(HttpStatusCode.OK, merchant)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get {
            val restaurants = merchantRepository.getAll().map { MerchantMapper.toMerchantDto(it) }
            if (restaurants.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, restaurants)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val restaurant = MerchantMapper.toMerchantDto(merchantRepository.get(id))
            if (restaurant != null) {
                call.respond(HttpStatusCode.OK, restaurant)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post {
            val restaurant = call.receive<MerchantDto>()
            val id = merchantRepository.add(MerchantMapper.toMerchantTable(restaurant))
            call.respond(HttpStatusCode.OK, MerchantDto(id))
        }

        put {
            val restaurant = call.receive<MerchantDto>()
            val updated = merchantRepository.update(MerchantMapper.toMerchantTable(restaurant))
            if (updated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.InternalServerError)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id != null) {
                val deleted = merchantRepository.delete(id)
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