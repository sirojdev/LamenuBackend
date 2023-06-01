package mimsoft.io.room

import mimsoft.io.entities.room.ROOM_TABLE_NAME
import mimsoft.io.entities.room.RoomTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
object RoomService {

    val repository: BaseRepository = DBManager
    suspend fun getAll(): List<RoomTable?> =
        repository.getData(dataClass = RoomTable::class, tableName = ROOM_TABLE_NAME).filterIsInstance<RoomTable?>()

    suspend fun get(id: Long?): RoomTable?  =
        repository.getData(dataClass = RoomTable::class, id = id, tableName = ROOM_TABLE_NAME).firstOrNull() as RoomTable?

    suspend fun add(tableTable: RoomTable?): Long? =
        repository.postData(dataClass = RoomTable::class, dataObject = tableTable, tableName = ROOM_TABLE_NAME)

    suspend fun update(tableTable: RoomTable?): Boolean =
        repository.updateData(dataClass = RoomTable::class, dataObject = tableTable, tableName = ROOM_TABLE_NAME)


    suspend fun delete(id: Long?) : Boolean =
        repository.deleteData(tableName = ROOM_TABLE_NAME, whereValue = id)
}