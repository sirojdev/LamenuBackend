package mimsoft.io.entities.room

import mimsoft.io.utils.DBManager
object RoomService : RoomRepository {

    override suspend fun getAll(): List<RoomTable?> =
        DBManager.getData(dataClass = RoomTable::class, tableName = ROOM_TABLE_NAME).filterIsInstance<RoomTable?>()

    override suspend fun get(id: Long?): RoomTable?  =
        DBManager.getData(dataClass = RoomTable::class, id = id, tableName = ROOM_TABLE_NAME).firstOrNull() as RoomTable?

    override suspend fun add(tableTable: RoomTable?): Long? =
        DBManager.postData(dataClass = RoomTable::class, dataObject = tableTable, tableName = ROOM_TABLE_NAME)

    override suspend fun update(tableTable: RoomTable?): Boolean =
        DBManager.updateData(dataClass = RoomTable::class, dataObject = tableTable, tableName = ROOM_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = ROOM_TABLE_NAME, whereValue = id)
}