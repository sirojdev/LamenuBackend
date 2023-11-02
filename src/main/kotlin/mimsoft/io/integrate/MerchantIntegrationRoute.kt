package mimsoft.io.integrate

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.integrate.MerchantIntegrateRepository

fun Route.merchantIntegrationRoute() {
  get("yandex-map") {
    val merchantId = call.parameters["appKey"]?.toLong()
    val merchantIntegrateDto = MerchantIntegrateRepository.get(merchantId)
    call.respond(merchantIntegrateDto ?: HttpStatusCode.NoContent)
  }
}
