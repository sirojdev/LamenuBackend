package mimsoft.io.room

object RoomMapper {
    fun toRoomTable(roomDto: RoomDto?): RoomTable? {
        return if (roomDto == null) null
        else RoomTable(
            id = roomDto.id,
            name = roomDto.name,
            flatId = roomDto.flatId,
            restaurantId = roomDto.restaurantId
        )
    }
    fun toRoomDto(roomTable: RoomTable?):RoomDto? {
        return if(roomTable == null) null
        else RoomDto(
            id = roomTable.id,
            name = roomTable.name,
            flatId = roomTable.flatId,
            restaurantId = roomTable.restaurantId
        )
    }

}