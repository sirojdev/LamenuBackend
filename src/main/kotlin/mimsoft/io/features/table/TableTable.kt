package mimsoft.io.features.table

import java.sql.Timestamp

const val TABLE_TABLE_NAME = "tables"

data class TableTable(
    val id: Long? = null,
    val name: String? = null,
    val roomId: Long? = null,
    val qr: String? = null,
    val branchId: Long? = null,
    val merchantId: Long? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
