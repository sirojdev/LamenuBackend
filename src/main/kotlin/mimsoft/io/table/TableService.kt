package mimsoft.io.table

import mimsoft.io.entities.table.TABLE_TABLE_NAME
import mimsoft.io.entities.table.TableTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object TableService {

    val repository: BaseRepository = DBManager
    suspend fun getAll(): List<TableTable?> =
        repository.getData(dataClass = TableTable::class, tableName = TABLE_TABLE_NAME).filterIsInstance<TableTable?>()

    suspend fun get(id: Long?): TableTable?  =
        repository.getData(dataClass = TableTable::class, id = id, tableName = TABLE_TABLE_NAME).firstOrNull() as TableTable?

    suspend fun add(tableTable: TableTable?): Long? =
        repository.postData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)


    suspend fun update(tableTable: TableTable?): Boolean =
        repository.updateData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)

    suspend fun delete(id: Long?) : Boolean =
        repository.deleteData(tableName = TABLE_TABLE_NAME, whereValue = id)

}