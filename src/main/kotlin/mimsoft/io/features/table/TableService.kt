package mimsoft.io.features.table

import mimsoft.io.entities.table.TABLE_TABLE_NAME
import mimsoft.io.entities.table.TableTable
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.GSON

object TableService : TableRepository {

    override suspend fun getAll(): List<TableTable?> =
        DBManager.getData(dataClass = TableTable::class, tableName = TABLE_TABLE_NAME).filterIsInstance<TableTable?>()

    override suspend fun get(id: Long?): TableTable?  =
        DBManager.getData(dataClass = TableTable::class, id = id, tableName = TABLE_TABLE_NAME).firstOrNull() as TableTable?

    override suspend fun add(tableTable: TableTable?): Long? {
        println("\nTable ${GSON.toJson(tableTable)}}" )
        return DBManager.postData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)

    }
    override suspend fun update(tableTable: TableTable?): Boolean =
        DBManager.updateData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = TABLE_TABLE_NAME, whereValue = id)

}