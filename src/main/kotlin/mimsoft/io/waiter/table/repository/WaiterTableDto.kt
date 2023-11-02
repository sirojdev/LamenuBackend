package mimsoft.io.waiter.table.repository

import java.sql.Timestamp
import mimsoft.io.features.table.TableDto

class WaiterTableDto(
  val id: Long? = null,
  val waiterId: Long? = null,
  val tableId: Long? = null,
  val joinTime: Timestamp? = null,
  val finishTime: Timestamp? = null,
  val table: TableDto? = null,
  val count: Int? = null
)
