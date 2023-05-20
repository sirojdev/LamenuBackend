package mimsoft.io.entities.option.repository

import mimsoft.io.entities.option.OPTION_TABLE_NAME
import mimsoft.io.entities.option.OptionTable
import mimsoft.io.utils.DBManager


object OptionRepositoryImpl : OptionRepository {
    override suspend fun getAll(): List<OptionTable?> =
        DBManager.getData(dataClass = OptionTable::class, tableName = OPTION_TABLE_NAME)
            .filterIsInstance<OptionTable?>()

    override suspend fun get(id: Long?): OptionTable? =
        DBManager.getData(dataClass = OptionTable::class, id = id, tableName = OPTION_TABLE_NAME)
            .firstOrNull() as OptionTable?

    override suspend fun add(optionTable: OptionTable?): Long? =
        DBManager.postData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun update(optionTable: OptionTable?): Boolean =
        DBManager.updateData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = OPTION_TABLE_NAME, id = id)
}