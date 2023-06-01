package mimsoft.io.room

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
object RoomService : RoomRepository {

    val repository: BaseRepository = DBManager
    override suspend fun getAll(): List<RoomTable?> =
        repository.getData(dataClass = RoomTable::class, tableName = ROOM_TABLE_NAME).filterIsInstance<RoomTable?>()

    override suspend fun get(id: Long?): RoomTable?  =
        repository.getData(dataClass = RoomTable::class, id = id, tableName = ROOM_TABLE_NAME).firstOrNull() as RoomTable?

    override suspend fun add(tableTable: RoomTable?): Long? =
        repository.postData(dataClass = RoomTable::class, dataObject = tableTable, tableName = ROOM_TABLE_NAME)

    override suspend fun update(tableTable: RoomTable?): Boolean =
        repository.updateData(dataClass = RoomTable::class, dataObject = tableTable, tableName = ROOM_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        repository.deleteData(tableName = ROOM_TABLE_NAME, whereValue = id)
}