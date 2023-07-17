package mimsoft.io.features.extra.ropository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.extra.EXTRA_TABLE_NAME
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.extra.ExtraMapper
import mimsoft.io.features.extra.ExtraTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp


object ExtraRepositoryImpl : ExtraRepository {
    val repository: BaseRepository = DBManager
    val mapper = ExtraMapper

    override suspend fun getAll(merchantId: Long?): List<ExtraTable?> {
        val data = repository.getPageData(
            dataClass = ExtraTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = "extra"
        )?.data

        return data ?: emptyList()
    }

    override suspend fun get(id: Long?, merchantId: Long?): ExtraTable? {
        val data = repository.getPageData(
            dataClass = ExtraTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = EXTRA_TABLE_NAME
        )?.data?.firstOrNull()
        return data
    }

    override suspend fun update(dto: ExtraDto): Boolean {
        val merchantId = dto.merchantId
        val query = "UPDATE $EXTRA_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " price = ${dto.price} ," +
                " product_id = ${dto.productId} ," +
                " description_uz = ?," +
                " description_ru = ?," +
                " description_eng = ?," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
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
        return true
    }

    override suspend fun add(extraTable: ExtraTable?): Long? =
        DBManager.postData(dataClass = ExtraTable::class, dataObject = extraTable, tableName = EXTRA_TABLE_NAME)

    suspend fun getExtrasByProductId(merchantId: Long?, productId: Long?): List<ExtraDto>? {
        val data = repository.getPageData(
            dataClass = ExtraTable::class,
            where = mapOf(
                ("merchant_id" to merchantId as Any),
                ("product_id" to productId as Any),
            ),
            tableName = EXTRA_TABLE_NAME
        )?.data

        return data?.map { mapper.toExtraDto(it)!! }
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $EXTRA_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use {it.prepareStatement(query).execute() }
        }
        return true
    }
}