package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.repository.MerchantInterface
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp

fun Route.routeToMerchantInfo() {
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
}