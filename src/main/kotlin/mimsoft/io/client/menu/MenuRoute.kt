package mimsoft.io.client.menu

import io.ktor.http.*
import io.ktor.http.cio.internals.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category.repository.CategoryRepository
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.product.ProductMapper
import mimsoft.io.features.product.repository.ProductRepository
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.utils.principal.MerchantPrincipal


fun Route.routeToMenu() {
    get("/menus") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl
        val categories = categoryRepository.getAll(merchantId=merchantId).map { it }

        val productRepository: ProductRepository = ProductRepositoryImpl
        val products = productRepository.getAll(merchantId=merchantId).map { ProductMapper.toProductDto(it)}

        val menus = MenuDto(
            categoryList = categories,
            productList = products
        )
        call.respond(HttpStatusCode.OK, menus)
    }
}
