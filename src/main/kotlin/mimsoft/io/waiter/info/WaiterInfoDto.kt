package mimsoft.io.waiter.info

import java.sql.Timestamp

data class WaiterInfoDto (
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: Timestamp? = null,
    val image: String? = null,
    val gender: String? = null,
    val status: Boolean? = null,
    val balance: Double? = null,
)