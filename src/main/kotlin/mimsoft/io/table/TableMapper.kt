package mimsoft.io.table

object TableMapper {
  fun toTableTable(tableDto: TableDto?): TableTable? {
    return if (tableDto == null) null
    else
      TableTable(
        id = tableDto.id,
        name = tableDto.name,
        roomId = tableDto.roomId,
        qr = tableDto.qr,
        restaurantId = tableDto.restaurantId
      )
  }

  fun toTableDto(tableTable: TableTable?): TableDto? {
    return if (tableTable == null) null
    else
      TableDto(
        id = tableTable.id,
        name = tableTable.name,
        roomId = tableTable.roomId,
        qr = tableTable.qr,
        restaurantId = tableTable.restaurantId
      )
  }
}
