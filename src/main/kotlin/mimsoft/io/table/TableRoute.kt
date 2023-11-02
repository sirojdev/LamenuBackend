package mimsoft.io.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToTable() {
  val tableService: TableRepository = TableService
  val tableMapper = TableMapper
  get("tables") {
    val tables = tableService.getAll().map { tableMapper.toTableDto(it) }
    if (tables.isEmpty()) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    } else call.respond(tables)
  }

  get("table/{id}") {
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val table = TableMapper.toTableDto(tableService.get(id))
    if (table == null) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    }
    call.respond(table)
  }

  post("table") {
    val table = call.receive<TableDto>()
    tableService.add(TableMapper.toTableTable(table))
    call.respond(HttpStatusCode.OK)
  }

  put("table") {
    val table = call.receive<TableDto>()
    tableService.update(TableMapper.toTableTable(table))
    call.respond(HttpStatusCode.OK)
  }

  delete("table/{id}") {
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@delete
    }
    tableService.delete(id)
    call.respond(HttpStatusCode.OK)
  }
}
