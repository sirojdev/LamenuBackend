package mimsoft.io.features.table

import mimsoft.io.features.branch.BranchDto

object TableMapper {
    fun toTableTable(tableDto: TableDto?): TableTable? {
        return if (tableDto == null) null
        else TableTable(
            id = tableDto.id,
            name = tableDto.name,
            qr = tableDto.qr,
            roomId = tableDto.roomId,
            branchId = tableDto.branch?.id,
            merchantId = tableDto.merchantId
        )
    }

    fun toTableDto(tableTable: TableTable?): TableDto? {
        return if (tableTable == null) null
        else TableDto(
            id = tableTable.id,
            name = tableTable.name,
            roomId = tableTable.roomId,
            qr = tableTable.qr,
            branch = BranchDto( tableTable.branchId),
            merchantId = tableTable.merchantId
        )
    }

}