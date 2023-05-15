package mimsoft.io.entities.log

import mimsoft.io.utils.TextModel
import java.sql.Timestamp

data class OrderLog(
    val id: Long? = null,
    val orderId : Long? = null,
    val title : TextModel? = null,
    val body : TextModel? = null,
    val createdAt : Timestamp? = null,
)
