package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.category.repository.CategoryRepositoryImpl.getCategoryByName
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.lamenu_bot.enums.Language

fun Route.routeToClientProduct() {
    val categoryRepository = CategoryRepositoryImpl
    val productRepository = ProductRepositoryImpl

    get("products") {
        val merchantId = call.parameters["id"]?.toLongOrNull()
        val categoryName = call.parameters["category"]
        val lang = Language.valueOf(call.parameters["lang"] ?: "UZ")
        if (merchantId == null || categoryName == null) {
            call.respond(HttpStatusCode.BadRequest)
        }
        val category = getCategoryByName(merchantId, lang, categoryName)
        if (category == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        val productList = productRepository.getAllByCategories(merchantId, category?.id)
        call.respond(HttpStatusCode.OK, productList)
    }

    get("product") {
        val merchantId = call.parameters["id"]?.toLongOrNull()
        val productName = call.parameters["product"]
        val lang = Language.valueOf(call.parameters["lang"] ?: "UZ")
        if (merchantId == null || productName == null) {
            call.respond(HttpStatusCode.BadRequest)
        }
        val product = productRepository.getByName(productName.toString(), lang, merchantId!!)
        if (product == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        call.respond(HttpStatusCode.OK, product!!)
    }

}