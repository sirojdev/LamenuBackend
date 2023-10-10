package mimsoft.io.features.room

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.book.BookStatus
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.sms_gateway.SMS_GATEWAY_TABLE
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.table.TableDto
import mimsoft.io.features.table.TableStatus
import mimsoft.io.features.visit.enums.CheckStatus
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.waiter.room.TableInfoDto
import java.sql.Timestamp

object RoomService : RoomRepository {
    val repository: BaseRepository = DBManager
    val mapper = RoomMapper
    override suspend fun getAll(merchantId: Long?, branchId: Long?): List<RoomDto?> {
        val data = repository.getPageData(
            dataClass = RoomTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "branch_id" to branchId as Any
            ),
            tableName = ROOM_TABLE_NAME
        )?.data

        return data?.map { mapper.toRoomDto(it) } ?: emptyList()
    }

    override suspend fun get(id: Long?, merchantId: Long?, branchId: Long?): RoomDto? {
        val data = repository.getPageData(
            dataClass = RoomTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "branch_id" to branchId as Any,
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
                updated   = ?
            where id = ${roomDto?.id}
              and merchant_id = ${roomDto?.merchantId}
              and branch_id = ${roomDto?.branchId}
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

    override suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean {
        var rs: Int
        val query = """
            update room
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and branch_id = $branchId
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            ProductRepositoryImpl.repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
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

    suspend fun getWithTableForWaiter(branchId: Long?, roomId: Long?): List<TableInfoDto?> {
        val query = """
             select t.id t_id,
                    t.name t_name,
                    t.room_id t_room_id,
                    t.status t_status,
                    b.id b_id,
                    b.time b_time,
                    b.status b_status,
                    b.visitor_count b_visit_count,
                    v.id v_id,
                    v.status v_status,
                    v.client_count v_client_count
             from tables t left join visit v on t.id = v.table_id and v.status <>'CLOSED'
             left join book b on t.id = b.table_id and b.status ='ACCEPTED'
             where t.room_id = $roomId and t.branch_id = $branchId and not t.deleted
        """.trimIndent()
        val list = mutableListOf<TableInfoDto>()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    list.add(
                        TableInfoDto(
                            id = rs.getLong("t_id"),
                            name = rs.getString("t_name"),
                            roomId = rs.getLong("t_room_id"),
                            status = rs.getString("t_status")?.let { TableStatus.valueOf(it) },
                            bookId = rs.getLong("b_id"),
                            bookingTime = rs.getTimestamp("b_time"),
                            bookingStatus = rs.getString("b_status")?.let { BookStatus.valueOf(it) },
                            bookingVisitorCount = rs.getInt("b_visit_count"),
                            visitId = rs.getLong("v_id"),
                            visitStatus = rs.getString("v_status")?.let { CheckStatus.valueOf(it) },
                            visitClientCount = rs.getInt("v_client_count")
                        )
                    )
                }
            }
        }
        return list
    }
}