package mimsoft.io.features.table

object TableMapper {
    fun toTableTable(tableDto: TableDto?): TableTable? {
        return if (tableDto == null) null
        else TableTable(
            id = tableDto.id,
            name = tableDto.name,
            qr = tableDto.qr,
            roomId = tableDto.roomId,
            branchId = tableDto.branchId,
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
            branchId = tableTable.branchId,
            merchantId = tableTable.merchantId
        )
    }

}