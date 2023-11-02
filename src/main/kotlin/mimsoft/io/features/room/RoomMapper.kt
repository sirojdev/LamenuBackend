package mimsoft.io.features.room

object RoomMapper {
  fun toRoomTable(roomDto: RoomDto?): RoomTable? {
    return if (roomDto == null) null
    else
      RoomTable(
        id = roomDto.id,
        name = roomDto.name,
        branchId = roomDto.branchId,
        merchantId = roomDto.merchantId
      )
  }

  fun toRoomDto(roomTable: RoomTable?): RoomDto? {
    return if (roomTable == null) null
    else
      RoomDto(
        id = roomTable.id,
        name = roomTable.name,
        branchId = roomTable.branchId,
        merchantId = roomTable.merchantId
      )
  }
}
