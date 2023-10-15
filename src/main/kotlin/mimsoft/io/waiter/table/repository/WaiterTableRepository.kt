package mimsoft.io.waiter.table.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.Order
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.waiter.info.WaiterInfoDto
import mimsoft.io.waiter.socket.WaiterNewOrderDto
import java.sql.ResultSet

object WaiterTableRepository {
    val repository: BaseRepository = DBManager
    suspend fun getActiveTablesWaiters(waiterId: Long?, limit: Int, offset: Int): DataPage<WaiterTableDto> {
        val query = "select count(*) over() as count,  " +
                "t.name t_name, r.name r_name " +
                "from waiter_table wt inner join tables t on wt.table_id = t.id " +
                "inner join room r on r.id = t.room_id " +
                "where wt.waiter_id = $waiterId " +
                "and not wt.deleted and not t.deleted and not r.deleted " +
                "and wt.finish_time is null order by join_time desc limit $limit offset $offset "
        val list = ArrayList<WaiterTableDto>()
        var total: Int? = null

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                while (rs.next()) {
                    val res = getTables(rs)
                    total = res.count
                    list.add(res)
                }
            }
        }
        return DataPage(list, total)
    }

    private fun getTables(rs: ResultSet): WaiterTableDto {
        return WaiterTableDto(
            id = rs.getLong("id"),
            waiterId = rs.getLong("waiter_id"),
            tableId = rs.getLong("table_id"),
            joinTime = rs.getTimestamp("join_time"),
            finishTime = rs.getTimestamp("finish_time"),
            table = TableDto(
                name = rs.getString("t_name"),
                room = RoomDto(
                    name = rs.getString("r_name")
                )
            ),
            count = rs.getInt("count")
        )
    }

    suspend fun getFinishedTablesWaiters(waiterId: Long?, limit: Int, offset: Int): DataPage<WaiterTableDto> {
        val query = """select count(*) over () as count, wt.*, t.name t_name, r.name r_name
            from waiter_table wt
                     inner join tables t on wt.table_id = t.id
                     inner join room r on r.id = t.room_id
            where wt.waiter_id = $waiterId
              and wt.deleted = false
              and t.deleted = false
              and r.deleted = false
              and wt.finish_time is not null
            order by join_time desc
            limit $limit offset $offset """.trimIndent()
        val list = ArrayList<WaiterTableDto>()
        var total: Int? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                while (rs.next()) {
                    val res = (getTables(rs))
                    list.add(res)
                    total = res.count
                }
            }
        }
        return DataPage(list, total)
    }


    suspend fun isOpenTable(tableId: Long?): Boolean {
        val query = "select * from $WAITER_TABLE_NAME " +
                " where table_id =$tableId and deleted = false and  finish_time is null "
        var isOpen = true;
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                if (rs.next()) {
                    isOpen = false;
                }
            }
        }
        return isOpen
    }

    suspend fun joinToWaiter(waiterId: Long?, tableId: Long?, branchId: Long?): Boolean {
        val query = "INSERT INTO waiter_table (waiter_id, table_id, join_time)\n" +
                "SELECT $waiterId,$tableId, now()\n" +
                "WHERE\n" +
                "  EXISTS (SELECT 1 FROM staff WHERE id = $waiterId and not deleted and branch_id = $branchId)\n" +
                "  AND EXISTS (SELECT 1 FROM tables WHERE id = $tableId and not deleted and branch_id = $branchId)\n" +
                "  AND NOT EXISTS (\n" +
                "    SELECT 1 FROM waiter_table\n" +
                "    WHERE  table_id = $tableId AND finish_time is null\n" +
                "    )\n" +
                "ON CONFLICT DO NOTHING;"

//        val getTableQuery = """select wt.*, t.name t_name,r.name r_name
//                    from waiter_table wt
//                             inner join tables t on wt.table_id = t.id
//                             inner join room r on r.id = t.room_id
//                    where wt.waiter_id = $waiterId
//                    and wt.table_id = $tableId
//                      and wt.deleted = false
//                      and t.deleted = false
//                      and r.deleted = false
//                      and wt.finish_time is null
//                    order by join_time desc
//                  """
//        var dto: WaiterTableDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeUpdate()
                if (rs == 1) {
                    return@withContext true
//                    val tableRs = it.prepareStatement(getTableQuery).apply {
//                    }.executeQuery()
//                    if (tableRs.next()) {
//                        dto = getTables(tableRs)
//                    }
                }
            }
        }
        return false
    }

    suspend fun joinToWaiter(waiterId: Long?, branchId: Long?, order: WaiterNewOrderDto): Boolean {
        val query = """
            INSERT INTO waiter_table (waiter_id, table_id, join_time, visit_id)
            SELECT $waiterId, ${order.tableId}, now(), ${order.visitId}
            WHERE
              EXISTS (SELECT 1 FROM room WHERE id = ${order.roomId} and not deleted and branch_id = ${branchId})
              AND EXISTS (SELECT 1 FROM waiter WHERE staff_id = $waiterId and not deleted and is_active = true)
              AND EXISTS (SELECT 1 FROM staff WHERE id = $waiterId and not deleted and branch_id = $branchId)
              AND EXISTS (SELECT 1 FROM tables WHERE id = ${order.tableId} and room_id = ${order.roomId} and not deleted and branch_id = $branchId)
              AND NOT EXISTS (
                SELECT 1 FROM waiter_table
                WHERE table_id = ${order.tableId} AND finish_time is null
              )
            ON CONFLICT DO NOTHING;
        """.trimIndent()
        var result = false
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeUpdate()
                result = rs == 1
            }
        }
        return result
    }

    suspend fun finishTable(waiterId: Long?, tableId: Long?): Boolean {
        val query = "update  $WAITER_TABLE_NAME " +
                " set finish_time = now() " +
                " where waiter_id = $waiterId  and table_id = $tableId and finish_time is null and deleted = false"
        var result = true
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
                if (rs != 1) {
                    result = false
                }
            }
        }
        return result
    }

    suspend fun getWaiterByTableId(tableId: Long?): WaiterInfoDto? {
        val query = "select s.phone " +
                "from waiter_table wt " +
                "         left join waiter w on wt.waiter_id = w.id " +
                "         left join staff s on w.staff_id = s.id " +
                "where wt.table_id = $tableId " +
                "  and not wt.deleted " +
                "  and not w.deleted " +
                "  and not s.deleted "
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    return@withContext WaiterInfoDto(
                        phone = rs.getString("phone")
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun update(order: Order): Boolean {
        val query = "update orders set products = ? where id = ${order.id} and not deleted"
        return withContext(DBManager.databaseDispatcher) {
            val response = repository.connection().use {
                it.prepareStatement(query).apply { this.closeOnCompletion() }.executeUpdate()
            }
            return@withContext response == 1
        }
    }
}