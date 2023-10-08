package mimsoft.io.features.table

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel
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

    override suspend fun getByRoomId(roomId: Long?, merchantId: Long?): TableTable? {
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
        println("\nTable ${GSON.toJson(tableTable)}}")
        return DBManager.postData(dataClass = TableTable::class, dataObject = tableTable, tableName = TABLE_TABLE_NAME)
    }

    override suspend fun update(dto: TableDto): Boolean {
        var rs = 0
        val query = """
            update tables
            set name         = ?,
                qr           = ?,
                type         = ${dto.type},
                room_id      = ${dto.room?.id},
                status       = ?,
                booking_time = ${dto.bookingTime},
                branch_id    = ${dto.branch?.id},
                updated      = ?
            where id = ${dto.id}
              and merchant_id = ${dto.merchantId}
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.setString(1, dto.name)
                    this.setString(2, dto.qr)
                    this.setString(3, dto.status?.name)
                    this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    override suspend fun getRoomWithTables(merchantId: Long?, branchId: Long?): ArrayList<RoomDto> {
        val query = """
          SELECT
                r.id AS room_id,
                r.name AS r_name,
                CASE
                WHEN COUNT(t.id) > 0
                    THEN json_agg(json_build_object('id', t.id, 'name', t.name, 'qr', t.qr, 'type', t.type))
                ELSE '[]'
                END AS tables
            FROM
                room r
                    left JOIN
                tables t ON r.id = t.room_id and t.deleted = false
            WHERE
                (t.deleted is null or t.deleted = false ) and r.deleted = false AND r.merchant_id = $merchantId AND r.branch_id =$branchId 
            GROUP BY
                r.name, r.id;
        """.trimIndent()
        val rooms: ArrayList<RoomDto> = ArrayList() // Initialize the ArrayList
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    val tablesJson = rs.getString("tables")
                    val roomDto = RoomDto(
                        id = rs.getLong("room_id"),
                        name = rs.getString("r_name"),
                        tables = Gson().fromJson(tablesJson, object : TypeToken<List<TableDto>>() {}.type)
                    )
                    rooms.add(roomDto)
                }
            }
        }
        return rooms
    }

    override suspend fun getTablesWaiter(roomId: Long?, branchId: Long?, merchantId: Long?): List<TableDto?> {
        val query =
            "select * from tables where room_id = $roomId and branch_id = $branchId and merchant_id = $merchantId and not deleted"
        val list = mutableListOf<TableDto>()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    val dto = TableDto(
                        id = rs.getLong("id"),
                        name = rs.getString("name"),
                        status = TableStatus.valueOf(rs.getString("status")),
                        type = rs.getInt("type")
                    )
                    list.add(dto)
                }
            }
        }
        return list
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        var rs = 0
        val query = """
            update tables
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            ProductRepositoryImpl.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    override suspend fun getByQr(url: String): TableDto? {
        val query = """
            select t.id          t_id,
                t.merchant_id t_merchant_id,
                qr,
                type,
                t.name        t_name,
                t.status      t_status,
                t.booking_time t_booking_time,
                r.id          r_id,
                r.name        r_name,
                b.id          b_id,
                b.merchant_id b_merchant_id,
                name_uz,
                name_ru,
                name_eng,
                longitude,
                latitude,
                address,
                open,
                close,
                (select json_build_object(
                           'id', v.id,
                           'merchantId', v.merchant_id,
                           'status', v.status,
                           'clientCount', v.client_count,
                           'isActive', v.is_active
                       )
                from visit v
                where t.status = 'ACTIVE'
                and v.table_id = t.id
                and v.is_active = true
                and not v.deleted) as json_data
                from tables t
                left join branch b on t.branch_id = b.id
                left join room r on t.room_id = r.id
                where t.qr = ?
                and not t.deleted
                and not b.deleted
        """.trimIndent()
        var dto: TableDto
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, url)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    val visitData = rs.getString("json_data")
                    if (visitData != null) {
                        val visit = Gson().fromJson(visitData, VisitDto::class.java)
                        dto = TableDto(
                            id = rs.getLong("t_id"),
                            merchantId = rs.getLong("t_merchant_id"),
                            qr = rs.getString("qr"),
                            type = rs.getInt("type"),
                            name = rs.getString("t_name"),
                            status = TableStatus.valueOf(rs.getString("t_status")),
                            bookingTime = rs.getInt("t_booking_time"),
                            room = RoomDto(
                                id = rs.getLong("r_id"),
                                name = rs.getString("r_name"),
                            ),
                            branch = BranchDto(
                                id = rs.getLong("b_id"),
                                merchantId = rs.getLong("b_merchant_id"),
                                open = rs.getString("open"),
                                close = rs.getString("close"),
                                name = TextModel(
                                    uz = rs.getString("name_uz"),
                                    ru = rs.getString("name_ru"),
                                    eng = rs.getString("name_eng")
                                ),
                                longitude = rs.getDouble("longitude"),
                                latitude = rs.getDouble("latitude"),
                                address = rs.getString("address")
                            ),
                            visit = visit
                        )
                    } else {
                        dto = TableDto(
                            id = rs.getLong("t_id"),
                            merchantId = rs.getLong("t_merchant_id"),
                            qr = rs.getString("qr"),
                            type = rs.getInt("type"),
                            name = rs.getString("t_name"),
                            room = RoomDto(
                                id = rs.getLong("r_id"),
                                name = rs.getString("r_name"),
                            ),
                            branch = BranchDto(
                                id = rs.getLong("b_id"),
                                merchantId = rs.getLong("b_merchant_id"),
                                open = rs.getString("open"),
                                close = rs.getString("close"),
                                name = TextModel(
                                    uz = rs.getString("name_uz"),
                                    ru = rs.getString("name_ru"),
                                    eng = rs.getString("name_eng")
                                ),
                                longitude = rs.getDouble("longitude"),
                                latitude = rs.getDouble("latitude"),
                                address = rs.getString("address")
                            )
                        )
                    }
                    return@withContext dto
                } else return@withContext null
            }
        }
    }
}