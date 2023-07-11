package mimsoft.io.features.table

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.GSON
import java.sql.Timestamp

object TableService : TableRepository {
    val repository: BaseRepository = DBManager
    val mapper = TableMapper

    override suspend fun getAll(merchantId: Long?): List<TableTable?> {
        val data = repository.getPageData(
            dataClass = TableTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = TABLE_TABLE_NAME
        )?.data

        return data ?: emptyList()
    }

    override suspend fun get(id: Long?, merchantId: Long?): TableTable? {
        val data = repository.getPageData(
            dataClass = TableTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = TABLE_TABLE_NAME
        )?.data?.firstOrNull()
        return data
    }

    override suspend fun getByTableId(roomId: Long?, merchantId: Long?): TableTable? {
        val data = repository.getPageData(
            dataClass = TableTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "room_id" to roomId as Any
            ),
            tableName = TABLE_TABLE_NAME
        )?.data?.firstOrNull()
        return data
    }

    override suspend fun add(tableTable: TableTable?): Long? {
        println("\nTable ${GSON.toJson(tableTable)}}" )
        return DBManager.postData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)
    }

    override suspend fun update(dto: TableDto): Boolean {
        val merchantId = dto.merchantId
        val query = "UPDATE $TABLE_TABLE_NAME " +
                "SET" +
                " name = ?, " +
                " qr = ?," +
                " room_id = ${dto.roomId}," +
                " branch_id = ${dto.branchId}, " +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name)
                    ti.setString(2, dto.qr)
                    ti.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $TABLE_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    override suspend fun getByQr(url: String): TableDto? {
            val data = repository.getPageData(
                dataClass = TableTable::class,
                where = mapOf(
                    "qr" to url as Any,
                ),
                tableName = TABLE_TABLE_NAME
            )?.data?.firstOrNull()
            return mapper.toTableDto(data)
        }
}