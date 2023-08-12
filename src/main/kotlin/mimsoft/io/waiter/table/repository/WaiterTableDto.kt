package mimsoft.io.waiter.table.repository

import java.sql.Timestamp

class WaiterTableDto(
    val id: Long? = null,
    val waiterId: Long? = null,
    val tableId: Long? = null,
    val joinTime: Timestamp? = null,
    val finishTime: Timestamp? = null,
)