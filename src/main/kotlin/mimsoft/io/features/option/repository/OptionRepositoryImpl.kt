package mimsoft.io.features.option.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.favourite.mapper
import mimsoft.io.features.label.LABEL_TABLE_NAME
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.label.LabelTable
import mimsoft.io.features.label.repository.LabelRepositoryImpl
import mimsoft.io.features.option.OPTION_TABLE_NAME
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.option.OptionMapper
import mimsoft.io.features.option.OptionTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.BaseRepository
import java.sql.Timestamp


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
                        nameEng = rs.getString("name_en"),
                        image = rs.getString("image"),
                        price = rs.getLong("price"),
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

    override suspend fun getAll(merchantId: Long?): List<OptionTable?> {
        val data = repository.getPageData(
            dataClass = OptionTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = OPTION_TABLE_NAME
        )?.data

        return data ?: emptyList()
    }

    suspend fun getOptionsByProductId(merchantId: Long?, productId: Long?): List<OptionDto>? {
        val data = repository.getPageData(
            dataClass = OptionTable::class,
            where = mutableMapOf(
                ("merchant_id" to merchantId as Any),
                ("product_id" to (productId ?: 0) as Any),
            ),
            tableName = OPTION_TABLE_NAME
        )?.data

        return data?.map { OptionMapper.toOptionDto(it)!! }
    }

    override suspend fun get(id: Long?, merchantId: Long?): OptionDto? {
        val where: Map<String, Any>
        if (merchantId != null) {
            where = mapOf(
                "id" to id as Any,
                "merchant_id" to merchantId as Any
            )
        } else where = mapOf("id" to id as Any)
        val data = repository.getPageData(
            dataClass = OptionTable::class,
            where = where,
            tableName = OPTION_TABLE_NAME
        )?.data?.firstOrNull()
        return OptionMapper.toOptionDto(data)
    }

    override suspend fun add(optionTable: OptionTable?): Long? =
        DBManager.postData(dataClass = OptionTable::class, dataObject = optionTable, tableName = OPTION_TABLE_NAME)

    override suspend fun update(dto: OptionDto): Boolean {
        var rs = 0
        val merchantId = dto.merchantId
        val query = "UPDATE $OPTION_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " image = ? ," +
                " price = ${dto.price}," +
                " parent_id = ${dto.parentId}," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            rs = StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name?.uz)
                    ti.setString(2, dto.name?.ru)
                    ti.setString(3, dto.name?.eng)
                    ti.setString(4, dto.image)
                    ti.setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return rs == 1
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        var rs = 0
        val query = "update $OPTION_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id and deleted = false"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }
}