package mimsoft.io.entities.room

interface RoomRepository {
    suspend fun getAll(): List<RoomTable?>
    suspend fun get(id: Long?): RoomTable?
    suspend fun add(roomTable: RoomTable?): Long?
    suspend fun update(roomTable: RoomTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}