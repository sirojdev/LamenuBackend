package mimsoft.io.entities.extra.ropository

import mimsoft.io.entities.extra.EXTRA_TABLE_NAME
import mimsoft.io.entities.extra.ExtraTable
import mimsoft.io.repository.DBManager


object ExtraRepositoryImpl : ExtraRepository {
    override suspend fun getAll(): List<ExtraTable?> =
        DBManager.getData(dataClass = ExtraTable::class, tableName = EXTRA_TABLE_NAME)
            .filterIsInstance<ExtraTable?>()

    override suspend fun get(id: Long?): ExtraTable? =
        DBManager.getData(dataClass = ExtraTable::class, id = id, tableName = EXTRA_TABLE_NAME).firstOrNull() as ExtraTable?

    override suspend fun add(extraTable: ExtraTable?): Long? =
        DBManager.postData(dataClass = ExtraTable::class, dataObject = extraTable, tableName = EXTRA_TABLE_NAME)


    override suspend fun update(extraTable: ExtraTable?): Boolean =
        DBManager.updateData(dataClass = ExtraTable::class, dataObject = extraTable, tableName = EXTRA_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = EXTRA_TABLE_NAME, whereValue = id)
}