package mimsoft.io.features.merchant_booking

import java.sql.Timestamp
import mimsoft.io.features.table.TableDto

data class MerchantBookResponseDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val table: TableDto? = null,
  val time: Timestamp? = null,
  val phone: String? = null,
  val comment: String? = null,
  val status: String? = null
)
