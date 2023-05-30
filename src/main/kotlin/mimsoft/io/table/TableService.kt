package mimsoft.io.table

import mimsoft.io.utils.DBManager
object TableService : TableRepository {

    override suspend fun getAll(): List<TableTable?> =
        DBManager.getData(dataClass = TableTable::class, tableName = TABLE_TABLE_NAME).filterIsInstance<TableTable?>()

    override suspend fun get(id: Long?): TableTable?  =
        DBManager.getData(dataClass = TableTable::class, id = id, tableName = TABLE_TABLE_NAME).firstOrNull() as TableTable?

    override suspend fun add(tableTable: TableTable?): Long? =
        DBManager.postData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)


    override suspend fun update(tableTable: TableTable?): Boolean =
        DBManager.updateData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = TABLE_TABLE_NAME, whereValue = id)

}