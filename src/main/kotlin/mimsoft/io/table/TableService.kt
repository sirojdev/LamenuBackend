package mimsoft.io.table

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object TableService : TableRepository {

    val repository: BaseRepository = DBManager
    val mapper = TableMapper
    override suspend fun getAll(): List<TableDto?> =
        repository.getData(dataClass = TableTable::class, tableName = TABLE_TABLE_NAME)
            .filterIsInstance<TableTable?>().map { mapper.toTableDto(it) }

    override suspend fun get(id: Long?): TableDto?  =
        DBManager.getData(dataClass = TableTable::class, id = id, tableName = TABLE_TABLE_NAME)
            .firstOrNull().let { mapper.toTableDto(it as TableTable) }

    override suspend fun add(tableDto: TableDto?): Long? =
        DBManager.postData(
            dataClass = TableTable::class,
            dataObject = mapper.toTableTable(tableDto),
            tableName = TABLE_TABLE_NAME)


    override suspend fun update(tableDto: TableDto?): Boolean =
        DBManager.updateData(
            dataClass = TableTable::class,
            dataObject = mapper.toTableTable(tableDto),
            tableName = TABLE_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = TABLE_TABLE_NAME, whereValue = id)

}