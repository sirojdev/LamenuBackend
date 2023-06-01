package mimsoft.io.table

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object TableService : TableRepository {

    val repository: BaseRepository = DBManager
    override suspend fun getAll(): List<TableTable?> =
        repository.getData(dataClass = TableTable::class, tableName = TABLE_TABLE_NAME).filterIsInstance<TableTable?>()

    override suspend fun get(id: Long?): TableTable?  =
        repository.getData(dataClass = TableTable::class, id = id, tableName = TABLE_TABLE_NAME).firstOrNull() as TableTable?

    override suspend fun add(tableTable: TableTable?): Long? =
        repository.postData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)


    override suspend fun update(tableTable: TableTable?): Boolean =
        repository.updateData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        repository.deleteData(tableName = TABLE_TABLE_NAME, whereValue = id)

}