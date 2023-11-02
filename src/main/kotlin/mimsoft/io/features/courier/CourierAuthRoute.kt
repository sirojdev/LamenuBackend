package mimsoft.io.features.courier

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.repository.MerchantAuthImp
import mimsoft.io.features.merchant.repository.MerchantAuthService
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.merchantAuthRoute() {
  val authService: MerchantAuthService = MerchantAuthImp

  //    post("auth") {
  //        val staff = call.receive<StaffDto>()
  //        val staffAuth = authService.auth(merchant)
  //         if (merchantAuth != null) {
  //            call.respond(merchantAuth)
  //        } else
  //            call.respond(HttpStatusCode.BadRequest)
  //    }

  authenticate("merchant") {
    post("logout") {
      val merchant = call.principal<MerchantPrincipal>()
      authService.logout(merchant?.uuid)
      call.respond(HttpStatusCode.OK)
    }
  }
}
