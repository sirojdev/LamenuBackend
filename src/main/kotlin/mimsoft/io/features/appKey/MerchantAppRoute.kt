package mimsoft.io.features.appKey

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToMerchantApp() {
  route("appKey") {
    post("add") {
      val principal = getPrincipal()
      val appKeyDto = call.receive<MerchantAppKeyDto>()
      call.respond(MerchantAppKeyRepository.add(appKeyDto))
    }

    get("all") {
      val pr = getPrincipal()
      val merchantId = call.parameters["merchantId"]?.toLongOrNull()
      call.respond(MerchantAppKeyRepository.getAll(merchantId))
    }
    get("/{id}") {
      val pr = getPrincipal()
      val id = call.parameters["id"]?.toLongOrNull()
      call.respond(MerchantAppKeyRepository.getByAppId(id) ?: HttpStatusCode.NotFound)
    }
    delete("/{id}") {
      val pr = getPrincipal()
      val id = call.parameters["id"]?.toLongOrNull()
      call.respond(MerchantAppKeyRepository.deleteByAppId(id))
    }
  }
}
