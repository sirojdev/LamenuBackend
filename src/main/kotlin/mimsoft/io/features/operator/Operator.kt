package mimsoft.io.features.operator

import java.sql.Timestamp

data class Operator (
    val id: Long? = null,
    val staffId: Long? = null,
    val merchantId: Long? = null,
    val pbxCode: Int? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)