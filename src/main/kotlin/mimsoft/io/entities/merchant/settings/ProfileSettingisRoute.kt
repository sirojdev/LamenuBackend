package mimsoft.io.entities.merchant.settings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.merchant.MerchantDto
import mimsoft.io.entities.merchant.MerchantMapper
import mimsoft.io.entities.merchant.repository.MerchantInterface
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp

fun Route.routeToProfileSettings() {
    val merchantRepository: MerchantInterface = MerchantRepositoryImp
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

    put {
        val restaurant = call.receive<MerchantDto>()
        val updated = merchantRepository.update(MerchantMapper.toMerchantTable(restaurant))
        if (updated) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }
}