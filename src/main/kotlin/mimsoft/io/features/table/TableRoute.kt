package mimsoft.io.features.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToTable(){
    val tableService : TableRepository = TableService
    val tableMapper = TableMapper
    get("tables"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val tables = tableService.getAll(merchantId=merchantId).map { tableMapper.toTableDto(it) }
        if(tables.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(tables)
    }

    get("table/{id}"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val table = TableMapper.toTableDto(tableService.get(id=id, merchantId=merchantId))
        if(table == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(table)
    }

    post ("table"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val table = call.receive<TableDto>()
        val toTable = TableMapper.toTableTable(table)
        tableService.add(toTable?.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    put ("table"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val table = call.receive<TableDto>()
        tableService.update(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("table/{id}"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        tableService.delete(id=id, merchantId=merchantId)
        call.respond(HttpStatusCode.OK)
    }







}