package mimsoft.io.waiter.room

import com.google.common.collect.Tables
import mimsoft.io.features.book.BookStatus
import mimsoft.io.features.table.TableDto
import mimsoft.io.features.table.TableStatus
import mimsoft.io.features.visit.enums.CheckStatus
import java.sql.Timestamp

class RoomWithTableDto(
    val roomId: Long? = null,
    val tables: List<TableInfoDto?>? = null
)

data class TableInfoDto(
    val id: Long? = null,
    val name: String? = null,
    val type: Int? = null,
    val roomId: Long? = null,
    val status: TableStatus? = null,
    val bookId: Long? = null,
    val bookingTime: Timestamp? = null,
    val bookingStatus: BookStatus? = null,
    val bookingVisitorCount: Int? = null,
    val visitId: Long? = null,
    val visitStatus: CheckStatus? = null,
    val visitClientCount: Int? = null
)