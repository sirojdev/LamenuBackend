package mimsoft.io.features.room

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.sms_gateway.SMS_GATEWAY_TABLE
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.table.TableDto
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

        return data?.map { mapper.toRoomDto(it) } ?: emptyList()
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
        var rs = 0
        val query = """
            update room
            set name      = ?,
                branch_id = ${roomDto?.branchId},
                updated   = ?
            where id = ${roomDto?.id}
              and merchant_id = ${roomDto?.merchantId}
              and not deleted 
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            SmsGatewayService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.setString(1, roomDto?.name)
                    this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $ROOM_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(DBManager.databaseDispatcher) {
            ProductRepositoryImpl.repository.connection().use { it.prepareStatement(query).execute() }
        }
        return true
    }

    override suspend fun getWithTable(branchId: Long?, merchantId: Long?): List<RoomTableDto?> {
        val query = """
             SELECT r.id,
                   r.name,
                   r.branch_id,
                   (SELECT json_agg(json_build_object(
                           'id', t.id,
                           'name', t.name,
                           'type', t.type,
                           'qr', t.qr
                       ))
                    FROM tables t
                    WHERE t.room_id = r.id
                      AND t.merchant_id = $merchantId
                      and not t.deleted) AS tables
            FROM room r
            WHERE r.merchant_id = $merchantId
              and r.branch_id = $branchId
              and r.deleted = false
            order by created
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val list = mutableListOf<RoomTableDto>()
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val tables = rs.getString("tables")
                    val typeToken = object : TypeToken<List<TableDto>>() {}.type
                    val tableList = Gson().fromJson<List<TableDto>>(tables, typeToken)
                    list.add(
                        RoomTableDto(
                            id = rs.getLong("id"),
                            name = rs.getString("name"),
                            tables = tableList
                        )
                    )
                }
                return@withContext list
            }
        }
    }
}