package mimsoft.io.waiter.book

import jakarta.validation.constraints.NotNull
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.BookStatus
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.table.TableDto
import java.sql.Timestamp

data class WaiterBookDto(
    val tableId: Long? = null,
    val client: UserDto? = null,
    val time: Timestamp? = null,
    val comment: String? = null,
    val visitorCount: Int? = null,
    var status: BookStatus? = null
)