package mimsoft.io.entities.menu

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.category.CategoryMapper
import mimsoft.io.entities.category.repository.CategoryRepository
import mimsoft.io.entities.category.repository.CategoryRepositoryImpl
import mimsoft.io.entities.product.ProductMapper
import mimsoft.io.entities.product.repository.ProductRepository
import mimsoft.io.entities.product.repository.ProductRepositoryImpl


fun Route.routeToMenu() {
    get("/menus") {
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl
        val categories = categoryRepository.getAll().map { it }

        val productRepository: ProductRepository = ProductRepositoryImpl
        val products = productRepository.getAll().map { ProductMapper.toProductDto(it)}

        val menus = MenuDto(
            categoryList = categories,
            productList = products
        )

        call.respond(HttpStatusCode.OK, menus)
    }


}
