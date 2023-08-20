package mimsoft.io.features.product.product_option

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToProductOption() {
    val productOptionService = ProductOptionService
    route("product/option") {
        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val productOptionDto = call.receive<ProductOptionDto>()
            if (productOptionDto.productId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (productOptionDto.optionId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val result = productOptionService.add(productOptionDto.copy(merchantId = merchantId))
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
            val response = productOptionService.getOptionsByProductId(productId = id, merchantId = merchantId)
            if (response.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else
                call.respond(HttpStatusCode.OK, response)
        }

        delete {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val productOptionDto = call.receive<ProductOptionDto>()
            if (productOptionDto.productId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (productOptionDto.optionId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = productOptionService.deleteProductOption(productOptionDto.copy(merchantId = merchantId))
            if (!response) {
                call.respond(HttpStatusCode.NoContent)
                return@delete
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}