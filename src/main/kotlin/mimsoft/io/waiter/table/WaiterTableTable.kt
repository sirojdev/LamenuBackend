package mimsoft.io.waiter.table

import java.sql.Timestamp

const val WAITER_TABLE_NAME = "waiter_table"

data class WaiterTableTable(
    val id: Long? = null,
    val waiterId: Long? = null,
    val tableId: Long? = null,
    val joinTime: Timestamp? = null,
    val finishTime: Timestamp? = null,
    val deleted: Boolean? = null
)
