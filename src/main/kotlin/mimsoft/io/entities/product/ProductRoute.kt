package mimsoft.io.entities.product

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.product.repository.ProductRepository
import mimsoft.io.entities.product.repository.ProductRepositoryImpl

fun Route.routeToProduct() {

    val productRepository: ProductRepository = ProductRepositoryImpl
    get("/products") {
        val products = productRepository.getAll().map { ProductMapper.toProductDto(it) }
        if (products.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, products)
    }

    get("/product/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val product = ProductMapper.toProductDto(productRepository.get(id))
        if (product != null) {
            call.respond(HttpStatusCode.OK, product)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/product") {
        val product = call.receive<ProductDto>()
        val id = productRepository.add(ProductMapper.toProductTable(product))
        call.respond(HttpStatusCode.OK, ProductId(id))
    }

    put("/product") {
        val product = call.receive<ProductDto>()
        val updated = productRepository.update(ProductMapper.toProductTable(product))
        if (updated) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    delete("/product/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = productRepository.delete(id)
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