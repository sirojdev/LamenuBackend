package mimsoft.io.entities.option.repository

import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mimsoft.io.entities.option.OPTION_TABLE_NAME
import mimsoft.io.entities.option.OptionDto
import mimsoft.io.entities.option.OptionMapper
import mimsoft.io.entities.option.OptionTable
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.BaseRepository


object OptionRepositoryImpl : OptionRepository {

    val repository: BaseRepository = DBManager
    override suspend fun getSubOptions(id: Long?): List<OptionDto?> {

        val query = "select * from $OPTION_TABLE_NAME where parent_id = $id and deleted = false order by id asc"
        return repository.connection().use {
            val rs = it.prepareStatement(query).executeQuery()
            val options = arrayListOf<OptionDto?>()
            while (rs.next()) {
                val op = OptionMapper.toOptionDto(
                    OptionTable(
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
                    )
                )?.copy(
                    options = getSubOptions(id = rs.getLong("id"))
                )
                options.add(op)

            }

            return@use options

        }
    }


    override suspend fun getAll(): List<OptionDto?> {
        val list = DBManager.getData(dataClass = OptionTable::class, tableName = OPTION_TABLE_NAME)
            .filterIsInstance<OptionTable?>().map { OptionMapper.toOptionDto(it) }
        return list
    }

    override suspend fun get(id: Long?): OptionDto? {
        return OptionMapper.toOptionDto(
            DBManager.getData(dataClass = OptionTable::class, id = id, tableName = OPTION_TABLE_NAME)
                .firstOrNull() as OptionTable?
        )?.copy(
            options = getSubOptions(id = id)
        )
    }

    override suspend fun add(optionTable: OptionTable?): Long? =
        DBManager.postData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun update(optionTable: OptionTable?): Boolean =
        DBManager.updateData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = OPTION_TABLE_NAME, whereValue = id)
}