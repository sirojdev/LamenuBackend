package mimsoft.io.features.waiters.tips

import java.sql.Timestamp

data class TipsModel(
    val id: Long? = null,
    val clientId: Long? = null,
    val waiterId: Long? = null,
    val amount: Long? = null,
    val time: Timestamp? = null,
    val tableId: Long? = null
)