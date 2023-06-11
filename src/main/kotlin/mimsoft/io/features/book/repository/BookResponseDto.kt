package mimsoft.io.features.book.repository

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.table.TableDto
import java.sql.Timestamp

data class BookResponseDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val client: UserDto? = null,
    val table: TableDto? = null,
    val time: Timestamp? = null
)
