package mimsoft.io.features.log

import java.sql.Timestamp
import mimsoft.io.utils.TextModel

data class OrderLog(
  val id: Long? = null,
  val orderId: Long? = null,
  val title: TextModel? = null,
  val body: TextModel? = null,
  val createdAt: Timestamp? = null,
)
