package mimsoft.io.waiter.table

import mimsoft.io.features.table.TableDto
import java.sql.Timestamp

data class WaiterTableDto(
    val id: Long? = null,
    val waiterId: Long? = null,
    val tableId: Long? = null,
    val joinTime: Timestamp? = null,
    val finishTime: Timestamp? = null,
    val table: TableDto? = null,
    val count: Int? = null
)