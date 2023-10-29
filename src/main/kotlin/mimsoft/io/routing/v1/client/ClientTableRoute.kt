package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.room.RoomRepository
import mimsoft.io.features.room.RoomService
import mimsoft.io.features.table.TableRepository
import mimsoft.io.features.table.TableService

fun Route.routeToClientTable() {
  val tableRepository: TableRepository = TableService
  val roomRepository: RoomRepository = RoomService

  get("tablesByRoom") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val branchId = call.parameters["branchId"]?.toLongOrNull()
    val rooms = roomRepository.getWithTable(branchId = branchId, merchantId = merchantId)
    call.respond(HttpStatusCode.OK, rooms)
  }

  get("tablesByRoom") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val branchId = call.parameters["branchId"]?.toLongOrNull()
    val rooms = tableRepository.getRoomWithTables(merchantId, branchId)
    call.respond(HttpStatusCode.OK, rooms)
  }

  get("table") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val roomId = call.parameters["roomId"]?.toLongOrNull()
    val branchId = call.parameters["branchId"]?.toLongOrNull()
    val qr = call.parameters["qr"].toString()
    if (roomId != null || merchantId != null) {
      val table =
        tableRepository.getByRoomId(roomId = roomId, merchantId = merchantId, branchId = branchId)
      if (table == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(table)
      return@get
    }
    if (merchantId == null && roomId == null && qr != null) {
      val result = TableService.getByQr(url = qr)
      if (result == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(HttpStatusCode.OK, result)
    }
  }
}
