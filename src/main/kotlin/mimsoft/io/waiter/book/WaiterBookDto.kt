package mimsoft.io.waiter.book

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.sql.Timestamp
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.BookStatus

data class WaiterBookDto(
  @field:NotNull val client: UserDto? = null,
  @field:Positive val visitorCount: Int? = null,
  @field:NotNull val time: Timestamp? = null,
  @field:Positive val tableId: Long? = null,
  val comment: String? = null,
  var status: BookStatus? = null
)
