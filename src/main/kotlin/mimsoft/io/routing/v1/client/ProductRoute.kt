package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepositoryImpl.getProductsByCategoryName
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.telegram_bot.Language

fun Route.routeToClientProduct() {
  val productRepository = ProductRepositoryImpl
  /** for tg bot */
  get("products") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val categoryName = call.parameters["category"]
    val lang = Language.valueOf(call.parameters["lang"] ?: "UZ")
    if (merchantId == null || categoryName == null) {
      call.respond(HttpStatusCode.BadRequest)
    }
    val products = getProductsByCategoryName(merchantId, lang, categoryName)
    if (products.isNullOrEmpty()) {
      call.respond(HttpStatusCode.NoContent)
    }
    call.respond(HttpStatusCode.OK, products)
  }
  /** for tg bot products with options */
  get("product") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val productName = call.parameters["product"]
    val lang = Language.valueOf(call.parameters["lang"] ?: "UZ")
    if (productName != null) {
      val product =
        productRepository.getProductWithOptions(productName.toString(), lang, merchantId!!)
      if (product == null) {
        call.respond(HttpStatusCode.NoContent)
      } else {
        call.respond(HttpStatusCode.OK, product)
      }
    }
  }

  get("product/info") {
    val id = call.parameters["id"]?.toLongOrNull()
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val branchId = call.parameters["branchId"]?.toLongOrNull()
    if (merchantId == null || id == null) {
      call.respond(HttpStatusCode.BadRequest)
    }
    val product =
      productRepository.getProductInfo(merchantId = merchantId, id = id, branchId = branchId)
    if (product == null) {
      call.respond(HttpStatusCode.NotFound)
    }
    call.respond(product!!)
  }
}
