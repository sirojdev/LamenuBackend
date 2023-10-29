package mimsoft.io.waiter.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.BookStatus
import mimsoft.io.features.book.repository.BookRepositoryImpl
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.table.TableDto
import mimsoft.io.features.table.TableService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.waiter.table.repository.WaiterTableRepository

fun Route.routeToWaitersTables() {
  val waiterTableRepository = WaiterTableRepository
  route("table") {
    put("join") {
      val staffPrincipal = call.principal<BasePrincipal>()
      val tableId = call.parameters["tableId"]?.toLong()
      val waiterId = staffPrincipal?.staffId
      val rs = waiterTableRepository.joinToWaiter(waiterId, tableId, staffPrincipal?.branchId)
      if (rs) {
        call.respond(HttpStatusCode.OK, rs)
      } else {
        call.respond(HttpStatusCode.MethodNotAllowed)
      }
    }
    get("active") {
      val staffPrincipal = call.principal<BasePrincipal>()
      val waiterId = staffPrincipal?.staffId
      val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val activeTables = waiterTableRepository.getActiveTablesWaiters(waiterId, limit, offset)
      if (activeTables.data?.isEmpty() == true) {
        call.respond(HttpStatusCode.NoContent)
      }
      call.respond(HttpStatusCode.OK, activeTables)
    }
    get("finished") {
      val staffPrincipal = call.principal<BasePrincipal>()
      val waiterId = staffPrincipal?.staffId
      val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val activeTables = waiterTableRepository.getFinishedTablesWaiters(waiterId, limit, offset)
      if (activeTables.data?.isEmpty() == true) {
        call.respond(HttpStatusCode.NoContent)
      }
      call.respond(HttpStatusCode.OK, activeTables)
    }
    put("finish") {
      val staffPrincipal = call.principal<BasePrincipal>()
      val tableId = call.parameters["tableId"]?.toLong()
      val waiterId = staffPrincipal?.staffId
      val rs = waiterTableRepository.finishTable(waiterId, tableId)
      if (rs) {
        call.respond(HttpStatusCode.OK)
      } else {
        call.respond(HttpStatusCode.NotFound)
      }
    }

    get("tables") {
      val principal = getPrincipal()
      val merchantId = principal?.merchantId
      val branchId = principal?.branchId
      val roomId = call.parameters["roomId"]?.toLongOrNull()
      val tables =
        TableService.getTablesWaiter(roomId = roomId, branchId = branchId, merchantId = merchantId)
      if (tables.isEmpty()) {
        call.respond(HttpStatusCode.NotFound)
        return@get
      }
      tables.map { it }.let { it1 -> call.respond(it1) }
    }

    post("client") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val client = call.receive<UserDto>()
      val user = UserRepositoryImpl.get(phone = client.phone, merchantId = merchantId)
      if (user == null) {
        val userId = UserRepositoryImpl.add(userDto = client.copy(merchantId = merchantId)).body
        call.respond(ResponseModel(body = "New user add with id $userId"))
      } else call.respond(HttpStatusCode.OK, user)
    }

    post("order/accept") {
      val pr = getPrincipal()
      val order = call.receive<Order>()
      val response =
        OrderService.updateStatus(
          orderId = order.id,
          merchantId = pr?.merchantId,
          status = OrderStatus.ACCEPTED
        )
      if (response != null) {
        call.respond(HttpStatusCode.OK)
        return@post
      }
    }

    post("create/book") {
      val pr = getPrincipal()
      val tableId = call.parameters["tableId"]?.toLongOrNull()
      val bookDto = call.receive<BookDto>()
      val response =
        BookRepositoryImpl.add(
          bookDto =
            bookDto.copy(
              table = TableDto(id = tableId),
              merchantId = pr?.merchantId,
              status = BookStatus.ACCEPTED
            )
        )
      call.respond(response)
    }

    patch("update/book") {
      val pr = getPrincipal()
      val bookDto = call.receive<BookDto>()
      val response =
        BookRepositoryImpl.update(
          bookDto = bookDto.copy(merchantId = pr?.merchantId, status = BookStatus.ACCEPTED)
        )
      call.respond(response)
    }

    put("update/order/product") {
      val pr = getPrincipal()
      val order = call.receive<Order>()
      WaiterTableRepository.update(order.copy(merchant = MerchantDto(id = pr?.merchantId)))
    }
  }
}
