package mimsoft.io.entities.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.label.LabelDto
import mimsoft.io.entities.label.LabelMapper
import mimsoft.io.entities.label.LabelTable
import mimsoft.io.entities.order.repository.OrderRepository
import mimsoft.io.entities.order.repository.OrderRepositoryImpl
import mimsoft.io.utils.Mapper

fun Route.routeToOrder() {

    val repository: OrderRepository = OrderRepositoryImpl

    get("/orders") {
        val orders = repository.getAll().map { it?.let { it1 -> Mapper.toDto<OrderTable, OrderDto>(it1) } }
        if (orders.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond (HttpStatusCode.OK, orders)
    }

    get("/order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val order = Mapper.toDto<OrderTable, OrderDto>(repository.get(id))
        if (order != null) {
            call.respond(HttpStatusCode.OK, order)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/order") {
        val order = call.receive<OrderDto>()
        val id = repository.add(Mapper.toTable<OrderDto, OrderTable>(order))
        call.respond(HttpStatusCode.OK, OrderId(id))
    }

    put("/order") {
        val order = call.receive<OrderDto>()
        repository.update(Mapper.toTable<OrderDto, OrderTable>(order))
        call.respond(HttpStatusCode.OK)
    }

    delete("/order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = repository.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

data class OrderId(
    val id: Long? = null
)