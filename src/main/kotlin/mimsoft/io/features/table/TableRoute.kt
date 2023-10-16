package mimsoft.io.features.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToTable() {
    val tableService: TableRepository = TableService
    val tableMapper = TableMapper
    get("tables") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val tables =
            tableService.getAll(merchantId = merchantId, branchId = branchId).map { tableMapper.toTableDto(it) }
        if (tables.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(tables)
    }

    get("tableByRoom") {
        val pr = getPrincipal()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val roomId = call.parameters["roomId"]?.toLongOrNull()
        if(roomId == null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val response = tableService.getTablesWaiter(roomId = roomId, merchantId = merchantId, branchId = branchId)
        if (response.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        else
            call.respond(response)
    }

    get("table/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val table = TableMapper.toTableDto(tableService.get(id = id, merchantId = merchantId, branchId = branchId))
        if (table == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(table)
    }

    post("table") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val table = call.receive<TableDto>()
        val toTable = TableMapper.toTableTable(table)
        tableService.add(toTable?.copy(merchantId = merchantId, branchId = branchId))
        call.respond(HttpStatusCode.OK)
    }

    put("table") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val table = call.receive<TableDto>()
        val response = tableService.update(table.copy(merchantId = merchantId, branch = BranchDto(id = branchId)))
        if (response) {
            call.respond(ResponseModel(body = "Successfully update"))
        } else {
            call.respond(HttpStatusCode.Conflict)
        }
    }

    delete("table/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val response = tableService.delete(id = id, merchantId = merchantId, branchId = branchId)
        call.respond(response)
    }
}