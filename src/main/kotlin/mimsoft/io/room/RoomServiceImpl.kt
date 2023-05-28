package mimsoft.io.room

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
object RoomServiceImpl : RoomService {

    val repository: BaseRepository = DBManager
    val mapper = RoomMapper

    override suspend fun getAll(): List<RoomDto?> =
        repository.getData(dataClass = RoomTable::class, tableName = ROOM_TABLE_NAME)
            .filterIsInstance<RoomTable?>().map { mapper.toRoomDto(it) }

    override suspend fun get(id: Long?): RoomDto?  =
        repository.getData(dataClass = RoomTable::class, id = id, tableName = ROOM_TABLE_NAME)
            .firstOrNull().let { mapper.toRoomDto(it as RoomTable) }

    override suspend fun add(roomDto: RoomDto?): Long? =
        repository.postData(
            dataClass = RoomTable::class,
            dataObject = mapper.toRoomTable(roomDto),
            tableName = ROOM_TABLE_NAME)

    override suspend fun update(roomDto: RoomDto?): Boolean =
        repository.updateData(
            dataClass = RoomTable::class,
            dataObject = mapper.toRoomTable(roomDto),
            tableName = ROOM_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        repository.deleteData(tableName = ROOM_TABLE_NAME, whereValue = id)
}