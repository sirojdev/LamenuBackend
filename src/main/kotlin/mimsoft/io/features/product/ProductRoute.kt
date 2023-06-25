package mimsoft.io.features.product

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.product.repository.ProductRepository
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToProduct() {

    val productRepository: ProductRepository = ProductRepositoryImpl
    val mapper = ProductMapper

    get("/products") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId

        val products = (productRepository.getAllProductInfo(merchantId = merchantId))
        if (products.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, products)
    }

    get("/product/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val product = productRepository.getProductInfo(merchantId = merchantId, id = id)
        if (product != null) {
            call.respond(product)
        } else {
            call.respond(HttpStatusCode.NoContent)
        }
    }

    post("/product") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val productInfo = call.receive<ProductInfoDto>()
        val product = productInfo.product
        val id = productRepository.add(mapper.toProductTable(product?.copy(merchantId = merchantId)))
        call.respond(HttpStatusCode.OK, ProductId(id))
    }

    put("/product") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val productInfo = call.receive<ProductInfoDto>()
        val product = productInfo.product
        val updated = productRepository.update((product?.copy(merchantId = merchantId)))
        if (updated) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    delete("/product/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
        } else {
            val deleted = productRepository.delete(id = id, merchantId = merchantId)
            call.respond(deleted)
        }

    }

    get("product/info/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val response = productRepository.getProductInfo(id = id, merchantId = merchantId)
        if (response == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else
            call.respond(HttpStatusCode.OK, response)
    }
}

data class ProductId(
    val id: Long? = null
)