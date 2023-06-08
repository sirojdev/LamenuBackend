package mimsoft.io.features.room
interface RoomRepository {
    suspend fun getAll(merchantId: Long?): List<RoomDto?>
    suspend fun get(id: Long?, merchantId: Long?): RoomDto?
    suspend fun add(roomTable: RoomTable?): Long?
    suspend fun update(roomDto: RoomDto?): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
        /*
    suspend fun get(id: Long?, merchantId: Long?): RoomDto? {
        val query = "select * from $ROOM_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            RoomService.repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext RoomMapper.toRoomDto(
                        RoomTable(
                            name = rs.getString("name"),
                            branchId = rs.getLong("branch_id"),
                        )
                    )
                } else return@withContext null
            }
        }*/
    //}

}