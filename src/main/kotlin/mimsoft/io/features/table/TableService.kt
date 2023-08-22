package mimsoft.io.features.table

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.staff.StaffService
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
        val merchantId = dto.merchantId
        val query = "UPDATE $TABLE_TABLE_NAME " +
                "SET" +
                " name = ?, " +
                " qr = ?," +
                " type = ${dto.type}," +
                " room_id = ${dto.room?.id}," +
                " branch_id = ${dto.branch?.id}, " +
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

    override suspend fun getRoomWithTables(merchantId: Long?, branchId: Long?): ArrayList<RoomDto> {
        val query = """
            SELECT
                r.id AS room_id,
                r.name as r_name,
                json_agg(json_build_object('id', t.id, 'name', t.name,'qr',t.qr,'type',t.type)) AS tables
            FROM room  r
            left join  tables t on r.id = t.room_id
            where t.deleted = false and r.deleted = false and t.deleted = false and r.merchant_id = $merchantId and r.branch_id =$branchId
            group by r.name, r.id
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

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $TABLE_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    override suspend fun getByQr(url: String): TableDto? {
        val query = """
            select
                t.id          t_id,
                t.merchant_id t_merchant_id,
                qr,
                type,
                t.name        t_name,
                r.id r_id,
                r.name r_name,
                b.id          b_id,
                b.merchant_id b_merchant_id,
                name_uz,
                name_ru,
                name_eng,
                longitude,
                latitude,
                address,
                open,
                close
            from tables t
            left join branch b on t.branch_id = b.id
            left join room r on t.room_id = r.id
            where t.qr = '$url'
                and not t.deleted
                and not b.deleted
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext TableDto(
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
                } else return@withContext null
            }
        }
    }
}