package mimsoft.io.waiter.table.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.orders.CourierOrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import okhttp3.internal.wait
import java.sql.ResultSet

object WaiterTableRepository {
    val repository: BaseRepository = DBManager
    suspend fun getActiveTablesWaiters(waiterId: Long?, tableId: Long?): ArrayList<WaiterTableDto> {
        val query = "select * from $WAITER_TABLE_NAME " +
                " where table_id = $tableId and waiter_id = $waiterId and  deleted =false and finished_time is null order by join_time "
        val list = ArrayList<WaiterTableDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                while (rs.next()) {
                    list.add(
                        getTables(rs)
                    )
                }
            }
        }
        return list

    }

    private fun getTables(rs: ResultSet): WaiterTableDto {
        return WaiterTableDto(
            id = rs.getLong("id"),
            waiterId = rs.getLong("waiter_id"),
            tableId = rs.getLong("table_id"),
            joinTime = rs.getTimestamp("join_time"),
            finishTime = rs.getTimestamp("finish_time"),
        )
    }

    suspend fun getFinishedTablesWaiters(waiterId: Long?, tableId: Long?) {
        val query = "select * from $WAITER_TABLE_NAME " +
                " where table_id = $tableId and waiter_id = $waiterId and  deleted =false and finished_time is not null order by finished_time "
        val list = ArrayList<WaiterTableDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                while (rs.next()) {
                    list.add(getTables(rs))
                }
            }
        }
    }

    suspend fun isOpenTable(tableId: Long?): Boolean {
        val query = "select * from $WAITER_TABLE_NAME " +
                " where table_id =$tableId and deleted = false and  finished_time is null "
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

    suspend fun joinToWaiter(waiterId: Long?, tableId: Long?) {
        val query = " insert into $WAITER_TABLE_NAME (waiter_id,table_id,join_time) " +
                " values($waiterId,$tableId,now()) "
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeUpdate()
            }
        }
    }
}