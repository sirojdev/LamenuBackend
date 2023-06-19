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

    get("/products") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val products = productRepository.getAll(merchantId = merchantId).map { ProductMapper.toProductDto(it) }
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
        val product = ProductMapper.toProductDto(productRepository.get(id = id, merchantId = merchantId))
        if (product != null) {
            call.respond(HttpStatusCode.OK, product)
        } else {
            call.respond(HttpStatusCode.NoContent)
        }
    }

    post("/product") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val product = call.receive<ProductDto>()
        val id = productRepository.add(ProductMapper.toProductTable(product.copy(merchantId = merchantId)))
        call.respond(HttpStatusCode.OK, ProductId(id))
    }

    put("/product") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val product = call.receive<ProductDto>()
        val updated = productRepository.update((product.copy(merchantId = merchantId)))
        if (updated) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    delete("/product/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = productRepository.delete(id = id, merchantId = merchantId)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else
            call.respond(HttpStatusCode.BadRequest)
    }
}

data class ProductId(
    val id: Long? = null
)