package mimsoft.io.features.room

import mimsoft.io.features.table.TableDto

data class RoomDto(
  val id: Long? = null,
  val name: String? = null,
  val branchId: Long? = null,
  val merchantId: Long? = null,
  val tables: List<TableDto>? = null
)
