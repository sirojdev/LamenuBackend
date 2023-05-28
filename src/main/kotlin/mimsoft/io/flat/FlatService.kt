package mimsoft.io.flat

import mimsoft.io.repository.DBManager

object FlatService : FlatRepository {

    override suspend fun getAll(): List<FlatTable?> =
        DBManager.getData(dataClass = FlatTable::class, tableName = FLAT_TABLE_NAME).filterIsInstance<FlatTable?>()

    override suspend fun get(id: Long?): FlatTable?  =
        DBManager.getData(dataClass = FlatTable::class, id = id, tableName = FLAT_TABLE_NAME).firstOrNull() as FlatTable?

    override suspend fun add(tableTable: FlatTable?): Long? =
        DBManager.postData(dataClass = FlatTable::class, dataObject = tableTable, tableName = FLAT_TABLE_NAME)

    override suspend fun update(tableTable: FlatTable?): Boolean =
        DBManager.updateData(dataClass = FlatTable::class, dataObject = tableTable, tableName = FLAT_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = FLAT_TABLE_NAME, whereValue = id)
}