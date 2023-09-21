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
            bookingTime = tableDto.bookingTime,
            status = tableDto.status?.name,
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
            bookingTime = tableTable.bookingTime,
            status = TableStatus.valueOf(tableTable.status.toString()),
            qr = tableTable.qr,
            branch = BranchDto(tableTable.branchId),
            merchantId = tableTable.merchantId
        )
    }

}