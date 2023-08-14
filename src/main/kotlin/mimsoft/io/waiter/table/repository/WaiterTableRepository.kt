package mimsoft.io.waiter.table.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import java.sql.ResultSet

object WaiterTableRepository {
    val repository: BaseRepository = DBManager
    suspend fun getActiveTablesWaiters(waiterId: Long?, limit: Int, offset: Int): DataPage<WaiterTableDto> {
        val query = """select wt.*, t.name t_name,r.name r_name
                    from waiter_table wt
                             inner join tables t on wt.table_id = t.id
                             inner join room r on r.id = t.room_id
                    where wt.waiter_id = $waiterId
                      and wt.deleted = false
                      and t.deleted = false
                      and r.deleted = false
                      and wt.finish_time is null
                    order by join_time desc 
                     limit $limit 
                     offset  $offset"""


        val countQuery = """select count(*) count
                             from waiter_table wt
                                      inner join tables t on wt.table_id = t.id
                                      inner join room r on r.id = t.room_id
                             where wt.waiter_id = $waiterId
                               and wt.deleted = false
                               and t.deleted = false
                               and r.deleted = false
                               and wt.finish_time is  null """
        val list = ArrayList<WaiterTableDto>()
        var total: Int? = null

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                while (rs.next()) {
                    list.add(
                        getTables(rs)
                    )
                }
                val crs = it.prepareStatement(countQuery).executeQuery()
                if (crs.next()) {
                    total = crs.getInt("count")
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
            )
        )
    }

    suspend fun getFinishedTablesWaiters(waiterId: Long?, limit: Int, offset: Int): DataPage<WaiterTableDto> {
        val query = """select wt.*, t.name t_name,r.name r_name
                       from waiter_table wt
                                inner join tables t on wt.table_id = t.id
                                inner join room r on r.id = t.room_id
                       where wt.waiter_id = $waiterId
                         and wt.deleted = false
                         and t.deleted = false
                         and r.deleted = false
                         and wt.finish_time is not null
                       order by join_time desc
                       limit $limit
                        offset  $offset """

        val countQuery = """select count(*) count
                             from waiter_table wt
                                      inner join tables t on wt.table_id = t.id
                                      inner join room r on r.id = t.room_id
                             where wt.waiter_id = $waiterId
                               and wt.deleted = false
                               and t.deleted = false
                               and r.deleted = false
                               and wt.finish_time is not null """
        val list = ArrayList<WaiterTableDto>()
        var total: Int? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                while (rs.next()) {
                    list.add(getTables(rs))
                }
                val crs = it.prepareStatement(countQuery).executeQuery()
                if (crs.next()) {
                    total = crs.getInt("count")
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

    suspend fun joinToWaiter(waiterId: Long?, tableId: Long?, merchantId: Long?): WaiterTableDto? {
        val query = "INSERT INTO waiter_table (waiter_id, table_id, join_time)\n" +
                "SELECT $waiterId,$tableId, now()\n" +
                "WHERE\n" +
                "    EXISTS (SELECT 1 FROM staff WHERE id = $waiterId)\n" +
                "  AND EXISTS (SELECT 1 FROM tables WHERE id = $tableId)\n" +
                "  AND NOT EXISTS (\n" +
                "    SELECT 1 FROM waiter_table\n" +
                "    WHERE  table_id = $tableId AND finish_time is null\n" +
                "    )\n" +
                "ON CONFLICT DO NOTHING;"

        val getTableQuery = """select wt.*, t.name t_name,r.name r_name
                    from waiter_table wt
                             inner join tables t on wt.table_id = t.id
                             inner join room r on r.id = t.room_id
                    where wt.waiter_id = $waiterId 
                    and wt.table_id = $tableId
                      and wt.deleted = false
                      and t.deleted = false
                      and r.deleted = false
                      and wt.finish_time is null
                    order by join_time desc 
                  """

        var dto: WaiterTableDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeUpdate()
                if (rs == 1) {
                    val tableRs = it.prepareStatement(getTableQuery).apply {
                    }.executeQuery()
                    if (tableRs.next()) {
                        dto = getTables(tableRs)
                    }
                }
            }
        }
        return dto
    }

    suspend fun finishTable(waiterId: Long?, tableId: Long?): Boolean {
        val query = "update  $WAITER_TABLE_NAME " +
                " set finish_time = now() " +
                " where waiter_id = $waiterId  and table_id = $tableId and finish_time is null and deleted = false"
        var result = true;
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeUpdate()
                if (rs != 1) {
                    result = false;
                }
            }
        }
        return result
    }
}