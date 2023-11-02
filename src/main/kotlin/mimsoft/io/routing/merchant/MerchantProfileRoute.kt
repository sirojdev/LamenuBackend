package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.repository.MerchantInterface
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToMerchantProfile() {
  val merchantRepository: MerchantInterface = MerchantRepositoryImp

  get("profile") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    if (merchantId == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val profile = merchantRepository.getMerchantById(merchantId)
    if (profile != null) {
      call.respond(HttpStatusCode.OK, profile)
    } else {
      call.respond(HttpStatusCode.NotFound)
    }
  }
}
