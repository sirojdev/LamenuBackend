package mimsoft.io.entities.option.repository

import mimsoft.io.entities.option.OPTION_TABLE_NAME
import mimsoft.io.entities.option.OptionDto
import mimsoft.io.entities.option.OptionTable
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.BaseRepository


object OptionRepositoryImpl : OptionRepository {

    val repository: BaseRepository = DBManager
    override suspend fun getSubOptions(id: Long?): List<OptionTable?> {

        val query = "select * from $OPTION_TABLE_NAME where parentId = $id and deleted = false order by id asc"
        repository.connection().use {
            val rs = it.prepareStatement(query).executeQuery()
            val options = arrayListOf<OptionTable>()
            while (rs.next()) {
                val op = OptionTable(
                    id = rs.getLong("id"),
                    parentId = rs.getLong("parent_id"),
                    nameUz = rs.getString("name_uz"),
                    nameRu = rs.getString("name_ru"),
                    nameEn = rs.getString("name_en"),
                    descriptionUz = rs.getString("description_uz"),
                    descriptionRu = rs.getString("description_ru"),
                    descriptionEn = rs.getString("description_en"),
                    image = rs.getString("image"),
                    price = rs.getDouble("price"),
                    deleted = rs.getBoolean("deleted"),
                    created = rs.getTimestamp("created"),
                    updated = rs.getTimestamp("updated"),
                    options = getSubOptions(rs.getLong("id"))
                )
                options.add(op)

            }

            return options



        }
    }


    override suspend fun getAll(): List<OptionTable?> {
        val list = DBManager.getData(dataClass = OptionTable::class, tableName = OPTION_TABLE_NAME)
            .filterIsInstance<OptionTable?>()
        return list
    }

    override suspend fun get(id: Long?): OptionTable? {
        val op = DBManager.getData(dataClass = OptionTable::class, id = id, tableName = OPTION_TABLE_NAME)
            .firstOrNull() as OptionTable?

        val subOps = getSubOptions(id)
        return op?.copy(options = subOps)
    }

    override suspend fun add(optionTable: OptionTable?): Long? =
        DBManager.postData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun update(optionTable: OptionTable?): Boolean =
        DBManager.updateData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = OPTION_TABLE_NAME, whereValue = id)
}