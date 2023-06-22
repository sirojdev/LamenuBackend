package mimsoft.io.features.product.product_label

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToProductLabel() {
    val productLabelService = ProductLabelService
    route("product/label") {
        post("") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val productLabelDto = call.receive<ProductLabelDto>()
            if (productLabelDto.productId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (productLabelDto.labelId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val result = productLabelService.add(productLabelDto.copy(merchantId = merchantId))
            call.respond(result)
        }

        get("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val response = productLabelService.getLabelsByProductId(productId = id, merchantId = merchantId)
            if (response.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else
                call.respond(HttpStatusCode.OK, response)
        }


        delete {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val productLabelDto = call.receive<ProductLabelDto>()
            if (productLabelDto.productId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (productLabelDto.labelId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = productLabelService.deleteProductLabel(productLabelDto.copy(merchantId = merchantId))
            if(!response){
                call.respond(HttpStatusCode.NoContent)
                return@delete
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}