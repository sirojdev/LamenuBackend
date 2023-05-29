package mimsoft.io.room

interface RoomService {
    suspend fun getAll(): List<RoomDto?>
    suspend fun get(id: Long?): RoomDto?
    suspend fun add(roomDto: RoomDto?): Long?
    suspend fun update(roomDto: RoomDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}