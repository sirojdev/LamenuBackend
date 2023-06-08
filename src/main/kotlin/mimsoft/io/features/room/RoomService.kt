package mimsoft.io.features.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.sms_gateway.SMS_GATEWAY_TABLE
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object RoomService : RoomRepository {
    val repository: BaseRepository = DBManager
    val mapper = RoomMapper
    override suspend fun getAll(merchantId: Long?): List<RoomDto?> {
        val data = repository.getPageData(
            dataClass = RoomTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = ROOM_TABLE_NAME
        )?.data

        return data?.map { mapper.toRoomDto(it)} ?: emptyList()
    }

    override suspend fun get(id: Long?, merchantId: Long?): RoomDto? {
        val data = repository.getPageData(
            dataClass = RoomTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = ROOM_TABLE_NAME
        )?.data?.firstOrNull()
        return mapper.toRoomDto(data)
    }

    override suspend fun add(roomTable: RoomTable?): Long? =
        DBManager.postData(dataClass = RoomTable::class, dataObject = roomTable, tableName = ROOM_TABLE_NAME)

    override suspend fun update(roomDto: RoomDto?): Boolean {
        val query = "update $ROOM_TABLE_NAME set " +
                "name = ?, " +
                "branch_id = ${roomDto?.branchId}, " +
                "updated = ? \n" +
                "where merchant_id = ${roomDto?.merchantId} and not deleted "
        withContext(Dispatchers.IO) {
            SmsGatewayService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, roomDto?.name)
                    this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    override suspend fun delete(id: Long?, merchantId: Long?) : Boolean {
        val query = "update $ROOM_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}