package mimsoft.io.features.label.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.label.LABEL_TABLE_NAME
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.label.LabelMapper
import mimsoft.io.features.label.LabelTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp


object LabelRepositoryImpl : LabelRepository {
    val repository: BaseRepository = DBManager
    val mapper = LabelMapper
    override suspend fun getAll(merchantId: Long?): List<LabelTable?> {
        val data = repository.getPageData(
            dataClass = LabelTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = LABEL_TABLE_NAME
        )?.data

        return data ?: emptyList()
    }

    override suspend fun get(id: Long?, merchantId: Long?): LabelTable? {
        val data = repository.getPageData(
            dataClass = LabelTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = LABEL_TABLE_NAME
        )?.data?.firstOrNull()
        return data
    }

    override suspend fun add(labelTable: LabelTable?): Long? =
        DBManager.postData(dataClass = LabelTable::class, dataObject = labelTable, tableName = LABEL_TABLE_NAME)

    override suspend fun update(dto: LabelDto): Boolean {
        var temp = 0
        val merchantId = dto.merchantId
        val query = "UPDATE $LABEL_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " text_color = ? ," +
                " bg_color = ?," +
                " icon = ?," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name?.uz)
                    ti.setString(2, dto.name?.ru)
                    ti.setString(3, dto.name?.eng)
                    ti.setString(4, dto.textColor)
                    ti.setString(5, dto.bgColor)
                    ti.setString(6, dto.icon)
                    ti.setTimestamp(7, Timestamp(System.currentTimeMillis()))
                    temp = ti.executeUpdate()
                }
            }
        }
        return temp == 1
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        var rs = 0
        val query = "update $LABEL_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }
}
