package mimsoft.io.features.product.product_extra

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToProductExtra() {
  val productExtraService = ProductExtraService
  route("product/extra") {
    post {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val productExtraDto = call.receive<ProductExtraDto>()
      if (productExtraDto.productId == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }
      if (productExtraDto.extraId == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }
      val result = productExtraService.add(productExtraDto.copy(merchantId = merchantId))
      call.respond(result)
    }

    get("{id}") {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val response =
        productExtraService.getExtrasByProductId(productId = id, merchantId = merchantId)
      if (response.isEmpty()) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      } else call.respond(HttpStatusCode.OK, response)
    }

    delete {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val productExtraDto = call.receive<ProductExtraDto>()
      if (productExtraDto.productId == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      if (productExtraDto.extraId == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val response =
        productExtraService.deleteProductExtra(productExtraDto.copy(merchantId = merchantId))
      if (!response) {
        call.respond(HttpStatusCode.NoContent)
        return@delete
      }
      call.respond(HttpStatusCode.OK, response)
    }
  }
}
