package mimsoft.io.features.book

import java.sql.Timestamp
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.table.TableDto

data class BookDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val branch: BranchDto? = null,
  val client: UserDto? = null,
  val table: TableDto? = null,
  val time: Timestamp? = null,
  val comment: String? = null,
  val visitorCount: Int? = null,
  var status: BookStatus? = null
)
