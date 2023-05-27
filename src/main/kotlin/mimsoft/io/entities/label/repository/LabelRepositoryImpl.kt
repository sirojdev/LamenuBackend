package mimsoft.io.entities.label.repository

import mimsoft.io.entities.label.LABEL_TABLE_NAME
import mimsoft.io.entities.label.LabelTable
import mimsoft.io.repository.DBManager


object LabelRepositoryImpl : LabelRepository {
    override suspend fun getAll(): List<LabelTable?> =
        DBManager.getData(dataClass = LabelTable::class, tableName = LABEL_TABLE_NAME)
            .filterIsInstance<LabelTable?>()

    override suspend fun get(id: Long?): LabelTable? =
        DBManager.getData(dataClass = LabelTable::class, id = id, tableName = LABEL_TABLE_NAME)
            .firstOrNull() as LabelTable?


    override suspend fun add(labelTable: LabelTable?): Long? =
        DBManager.postData(dataClass = LabelTable::class, dataObject = labelTable, tableName = LABEL_TABLE_NAME)


    override suspend fun update(labelTable: LabelTable?): Boolean =
        DBManager.updateData(dataClass = LabelTable::class, dataObject = labelTable, tableName = LABEL_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = LABEL_TABLE_NAME, whereValue = id)
}
