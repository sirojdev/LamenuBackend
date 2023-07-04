package mimsoft.io.client.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.room.RoomRepository
import mimsoft.io.features.room.RoomService
import mimsoft.io.features.table.TableMapper
import mimsoft.io.features.table.TableRepository
import mimsoft.io.features.table.TableService

fun Route.routeToClientTable(){
    val tableRepository: TableRepository = TableService
    val roomRepository: RoomRepository = RoomService

    get("tables") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val table = tableRepository.getAll(merchantId=merchantId).map { TableMapper.toTableDto(it) }

        val rooms = roomRepository.getAll(merchantId=merchantId).map { it }
        val tables = ClientTableDto(
            tableList = table,
            roomList = rooms
        )
        call.respond(HttpStatusCode.OK, tables)
    }

    get("table") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val roomId = call.parameters["roomId"]?.toLongOrNull()
        if (roomId==null || merchantId==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val table = tableRepository.getByTableId(roomId = roomId, merchantId = merchantId)
        if (table==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(table)
    }
}