package mimsoft.io.room

interface RoomRepository {
    suspend fun getAll(): List<RoomTable?>
    suspend fun get(id: Long?): RoomTable?
    suspend fun add(tableTable: RoomTable?): Long?
    suspend fun update(tableTable: RoomTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}