package mimsoft.io.entities.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.label.LabelDto
import mimsoft.io.entities.label.LabelMapper
import mimsoft.io.entities.order.repository.OrderRepository
import mimsoft.io.entities.order.repository.OrderRepositoryImpl

fun Route.routeToLabel() {

    val repository: OrderRepository = OrderRepositoryImpl

    get("/orders") {
        val orders = repository.getAll().map { LabelMapper.toLabelDto(it) }
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
        val order = LabelMapper.toLabelDto(repository.get(id))
        if (order != null) {
            call.respond(HttpStatusCode.OK, order)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/order") {
        val order = call.receive<LabelDto>()
        val id = repository.add(LabelMapper.toLabelTable(order))
        call.respond(HttpStatusCode.OK, LabelId(id))
    }

    put("/order") {
        val label = call.receive<LabelDto>()
        repository.update(LabelMapper.toLabelTable(label))
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

data class LabelId(
    val id: Long? = null
)