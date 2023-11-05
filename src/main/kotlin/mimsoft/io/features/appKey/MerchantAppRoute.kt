package mimsoft.io.features.appKey

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToMerchantApp() {
  route("appKey") {
    post("add") {
      val appKeyDto = call.receive<MerchantAppKeyDto>()
      call.respond(MerchantAppKeyRepository.add(appKeyDto))
    }

    get("all") {
      val merchantId = call.parameters["merchantId"]?.toLongOrNull()
      call.respond(MerchantAppKeyRepository.getAll(merchantId))
    }
    get("/{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      call.respond(MerchantAppKeyRepository.getByAppId(id) ?: HttpStatusCode.NotFound)
    }
    delete("/{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      call.respond(MerchantAppKeyRepository.deleteByAppId(id))
    }
  }
}
