package mimsoft.io.operator

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.min
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToOrderOperator() {

  route("order") {
    get("count") {
      val pr = getPrincipal()
      val branchId = call.parameters["branchId"]?.toLongOrNull()
      if (branchId == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val response =
        OrderService.getOrderCountStatus(merchant = pr?.merchantId, branchId = branchId)
      if (response.isEmpty()) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(response)
    }

    get {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = call.parameters["branchId"]?.toLongOrNull()
      val search = call.parameters["search"]
      val filter = call.parameters["filter"]
      val limit = min(call.parameters["limit"]?.toInt() ?: 20, 50)
      val offset = call.parameters["offset"]?.toInt() ?: 0
      val orders =
        OrderService.getForAdmin(
          merchantId = merchantId,
          branchId = branchId,
          search = search,
          filter = filter,
          limit = limit,
          offset = offset,
          statuses =
            """
                        '${OrderStatus.CANCELED.name}',
                        '${OrderStatus.CLOSED.name})'
                """
        )
      if (orders.data?.isEmpty() == true) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(orders)
    }

    get("active") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = call.parameters["branchId"]?.toLongOrNull()
      val search = call.parameters["search"]
      val filter = call.parameters["filter"]
      val limit = min(call.parameters["limit"]?.toInt() ?: 20, 50)
      val offset = call.parameters["offset"]?.toInt() ?: 0
      val orders =
        OrderService.getForAdmin(
          merchantId = merchantId,
          branchId = branchId,
          search = search,
          filter = filter,
          limit = limit,
          offset = offset,
          statuses =
            """
                        '${OrderStatus.OPEN.name}',
                        '${OrderStatus.ACCEPTED.name})',
                        '${OrderStatus.COOKING.name})',
                        '${OrderStatus.DONE.name})',
                        '${OrderStatus.ONWAY.name})',
                        '${OrderStatus.DELIVERED.name})'
                """
        )
      if (orders.data?.isEmpty() == true) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(orders)
    }

    get("history") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = call.parameters["branchId"]?.toLongOrNull()
      val search = call.parameters["search"]
      val filter = call.parameters["filter"]
      val limit = min(call.parameters["limit"]?.toInt() ?: 20, 50)
      val offset = call.parameters["offset"]?.toInt() ?: 0
      val orders =
        OrderService.orderHistory(
          merchantId = merchantId,
          branchId = branchId,
          search = search,
          filter = filter,
          limit = limit,
          offset = offset
        )
      if (orders.data?.isEmpty() == true) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(orders)
    }

    get("{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      val response =
        OrderService.getById(id = id, "user", "branch", "products", "payment_type", "courier")
      if (response == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(response)
    }

    post {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val order = call.receive<Order>()
      val response =
        OrderService.post(
          order =
            order.copy(merchant = MerchantDto(id = merchantId))
        )
      call.respond(response)
    }
  }

  route("product") {
    get {
      val pr = getPrincipal()
      val categoryId = call.parameters["categoryId"]?.toLongOrNull()
      val merchantId = pr?.merchantId
      val response =
        CategoryRepositoryImpl.getCategoryForClientById(id = categoryId, merchantId = merchantId)
      if (response == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(response)
    }
  }
}
