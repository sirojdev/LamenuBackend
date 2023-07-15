package mimsoft.io.features.table

import mimsoft.io.features.branch.BranchDto

data class TableDto (
    val id: Long? = null,
    val qr: String? = null,
    val name: String? = null,
    val roomId: Long? = null,
    val branch: BranchDto? = null,
    val merchantId: Long? = null
)