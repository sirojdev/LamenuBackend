package mimsoft.io.waiter.info

import mimsoft.io.features.table.TableDto
import java.sql.Timestamp

data class WaiterInfoDto (
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val birthDay: Timestamp? = null,
    val image: String? = null,
    val gender: String? = null,
    val status: Boolean? = null,
    val balance: Double? = null,
    val grade: Double? = null,
    val tables: List<TableDto>? = null
)