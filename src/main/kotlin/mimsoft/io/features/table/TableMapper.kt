package mimsoft.io.features.table

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.room.RoomDto

object TableMapper {
    fun toTableTable(tableDto: TableDto?): TableTable? {
        return if (tableDto == null) null
        else TableTable(
            id = tableDto.id,
            name = tableDto.name,
            type = tableDto.type,
            qr = tableDto.qr,
            roomId = tableDto.room?.id,
            branchId = tableDto.branch?.id,
            merchantId = tableDto.merchantId
        )
    }

    fun toTableDto(tableTable: TableTable?): TableDto? {
        return if (tableTable == null) null
        else TableDto(
            id = tableTable.id,
            name = tableTable.name,
            type = tableTable.type,
            room = RoomDto(
                id = tableTable.roomId
            ),
            qr = tableTable.qr,
            branch = BranchDto(tableTable.branchId),
            merchantId = tableTable.merchantId
        )
    }

}