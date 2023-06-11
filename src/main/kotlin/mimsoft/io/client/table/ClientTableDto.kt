package mimsoft.io.client.table

import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.table.TableDto

data class ClientTableDto(
    var roomList: List<RoomDto?>,
    var tableList: List<TableDto?>
)